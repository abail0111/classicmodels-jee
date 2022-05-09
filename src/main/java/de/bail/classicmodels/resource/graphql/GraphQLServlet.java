package de.bail.classicmodels.resource.graphql;

import de.bail.classicmodels.model.enities.*;
import de.bail.classicmodels.resource.graphql.datafetcher.mutation.*;
import de.bail.classicmodels.resource.graphql.datafetcher.query.*;
import de.bail.classicmodels.service.*;
import graphql.kickstart.servlet.GraphQLConfiguration;
import graphql.kickstart.servlet.GraphQLHttpServlet;
import graphql.schema.*;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.dataloader.BatchLoader;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderFactory;
import org.dataloader.DataLoaderRegistry;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * GraphQLServlet
 * Create a GraphQL Servlet
 */
@WebServlet(name = "GraphQLServlet", urlPatterns = {"graphql/*"}, loadOnStartup = 1)
public class GraphQLServlet extends GraphQLHttpServlet {

    @Inject
    private CustomerService customerService;

    @Inject
    private EmployeeService employeeService;

    @Inject
    private OfficeService officeService;

    @Inject
    private OrderService orderService;

    @Inject
    private OrderDetailService orderDetailService;

    @Inject
    private PaymentService paymentService;

    @Inject
    private ProductService productService;

    @Inject
    private ProductLineService productLineService;

    @Override
    protected GraphQLConfiguration getConfiguration() {
        return GraphQLConfiguration
                .with(createSchema())
                .with(new CustomGraphQLContextBuilder(buildDataLoaderRegistry()))
                .build();
    }

    /**
     * Create executable GraphQL Schema:
     * - load schema from class path
     * - create type registry
     * - create runtime wiring
     * @return Executable GraphQL Schema
     */
    private GraphQLSchema createSchema() {
        SchemaParser schemaParser = new SchemaParser();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        File schemaFile = new File(Objects.requireNonNull(GraphQLServlet.class.getClassLoader().getResource("schema.graphqls")).getFile());
        TypeDefinitionRegistry typeRegistry = schemaParser.parse(schemaFile);
        RuntimeWiring wiring = buildRuntimeWiring();
        return schemaGenerator.makeExecutableSchema(typeRegistry, wiring);
    }

    /**
     * Create runtime wiring with
     * Data Fetcher, Type Resolver and Data Loader
     * @return Runtime wiring
     */
    private RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                // Query Data Fetcher
                .type("Query", typeWiring -> typeWiring
                          .dataFetcher("customer", new CustomerDataFetcher(customerService))
                          .dataFetcher("customers", new CustomersDataFetcher(customerService))
                          .dataFetcher("employee", new EmployeeDataFetcher(employeeService))
                          .dataFetcher("employees", new EmployeesDataFetcher(employeeService))
                          .dataFetcher("office", new OfficeDataFetcher(officeService))
                          .dataFetcher("offices", new OfficesDataFetcher(officeService))
                          .dataFetcher("order", new OrderDataFetcher(orderService))
                          .dataFetcher("orders", new OrdersDataFetcher(orderService))
                          .dataFetcher("payment", new PaymentDataFetcher(paymentService))
                          .dataFetcher("payments", new PaymentsDataFetcher(paymentService))
                          .dataFetcher("product", new ProductDataFetcher(productService))
                          .dataFetcher("products", new ProductDataFetcher(productService))
                          .dataFetcher("productLine", new ProductLineDataFetcher(productLineService))
                          .dataFetcher("productLines", new ProductLinesDataFetcher(productLineService))
                          .dataFetcher("searchContact", new SearchContactsDataFetcher(customerService, employeeService))
                )
                // Mutation Data Fetcher
                .type("Mutation", typeWiring -> typeWiring
                        // create
                        .dataFetcher("createCustomer", new CreateCustomerDataFetcher(customerService))
                        .dataFetcher("createEmployee", new CreateEmployeeDataFetcher(employeeService))
                        .dataFetcher("createOffice", new CreateOfficeDataFetcher(officeService))
                        .dataFetcher("createOrder", new CreateOrderDataFetcher(orderService))
                        .dataFetcher("createOrderDetail", new CreateOrderDetailsDataFetcher(orderDetailService))
                        .dataFetcher("createPayment", new CreatePaymentDataFetcher(paymentService))
                        .dataFetcher("createProduct", new CreateProductDataFetcher(productService))
                        .dataFetcher("createProductLine", new CreateProductLineDataFetcher(productLineService))
                        // update
                        .dataFetcher("updateCustomer", new UpdateCustomerDataFetcher(customerService))
                        .dataFetcher("updateEmployee", new UpdateEmployeeDataFetcher(employeeService))
                        .dataFetcher("updateOffice", new UpdateOfficeDataFetcher(officeService))
                        .dataFetcher("updateOrder", new UpdateOrderDataFetcher(orderService))
                        .dataFetcher("updateOrderDetail", new UpdateOrderDetailsDataFetcher(orderDetailService))
                        .dataFetcher("updatePayment", new UpdatePaymentDataFetcher(paymentService))
                        .dataFetcher("updateProduct", new UpdateProductDataFetcher(productService))
                        .dataFetcher("updateProductLine", new UpdateProductLineDataFetcher(productLineService))
                        // delete
                        .dataFetcher("deleteCustomer", new DeleteCustomerDataFetcher(customerService))
                        .dataFetcher("deleteEmployee", new DeleteEmployeeDataFetcher(employeeService))
                        .dataFetcher("deleteOffice", new DeleteOfficeDataFetcher(officeService))
                        .dataFetcher("deleteOrder", new DeleteOrderDataFetcher(orderService))
                        .dataFetcher("deleteOrderDetail", new DeleteOrderDetailDataFetcher(orderDetailService))
                        .dataFetcher("deletePayment", new DeletePaymentDataFetcher(paymentService))
                        .dataFetcher("deleteProduct", new DeleteProductDataFetcher(productService))
                        .dataFetcher("deleteProductLine", new DeleteProductLineDataFetcher(productLineService))

                )
                // Contact Interface Type Resolver
                .type("Contact", typeWiring -> typeWiring
                        .typeResolver(typeResolutionEnvironment -> {
                            Object javaObject = typeResolutionEnvironment.getObject();
                            if (javaObject instanceof Customer) {
                                return typeResolutionEnvironment.getSchema().getObjectType("Customer");
                            } else {
                                return typeResolutionEnvironment.getSchema().getObjectType("Employee");
                            }
                        })

                )
                // Data Loader
                .type("Employee", typeWiring -> typeWiring
                        .dataFetcher("customer", dataFetchingEnvironment -> {
                            Employee employee = dataFetchingEnvironment.getSource();
                            DataLoader<Integer, Object> dataLoader = dataFetchingEnvironment.getDataLoader("customerDataLoader");
                            return dataLoader.load(employee.getId());
                        })
                )
                .type("Customer", typeWiring -> typeWiring
                        .dataFetcher("payments", dataFetchingEnvironment -> {
                            Customer customer = dataFetchingEnvironment.getSource();
                            DataLoader<Integer, Object> dataLoader = dataFetchingEnvironment.getDataLoader("paymentDataLoader");
                            return dataLoader.load(customer.getId());
                        })
                        .dataFetcher("order", dataFetchingEnvironment -> {
                            Customer customer = dataFetchingEnvironment.getSource();
                            DataLoader<Integer, Object> dataLoader = dataFetchingEnvironment.getDataLoader("orderDataLoader");
                            return dataLoader.load(customer.getId());
                        })
                )
                .type("Office", typeWiring -> typeWiring
                        .dataFetcher("employees", dataFetchingEnvironment -> {
                            Office office = dataFetchingEnvironment.getSource();
                            DataLoader<Integer, Object> dataLoader = dataFetchingEnvironment.getDataLoader("employeesDataLoader");
                            return dataLoader.load(office.getId());
                        })
                )
                .type("Order", typeWiring -> typeWiring
                        .dataFetcher("details", dataFetchingEnvironment -> {
                            Order order = dataFetchingEnvironment.getSource();
                            DataLoader<Integer, Object> dataLoader = dataFetchingEnvironment.getDataLoader("orderDetailsDataLoader");
                            return dataLoader.load(order.getId());
                        })
                )
                .type("OrderDetail", typeWiring -> typeWiring
                        .dataFetcher("product", dataFetchingEnvironment -> {
                            OrderDetail orderDetail = dataFetchingEnvironment.getSource();
                            DataLoader<String, Object> dataLoader = dataFetchingEnvironment.getDataLoader("productDataLoader");
                            return dataLoader.load(orderDetail.getProduct().getId());
                        })
                )
                .build();
    }

    /**
     * Crete Data Loader Registry with Batch Loader
     * @return Data Loader Registry
     */
    private DataLoaderRegistry buildDataLoaderRegistry() {
        DataLoaderRegistry dataLoaderRegistry = new DataLoaderRegistry();
        // Customer Data Loader
        dataLoaderRegistry.register("customerDataLoader", DataLoaderFactory.newDataLoader(new BatchLoader<Integer, List<Customer>>() {
            @Override
            public CompletionStage<List<List<Customer>>> load(List<Integer> list) {
                // load all customer by employee id
                List<Customer> customer = customerService.getAllCustomerByEmployees(list);
                // map employees to office
                Map<Integer, List<Customer>> customerMap = customer.stream().collect(Collectors.groupingBy(c -> c.getSalesRepEmployee().getId(), HashMap::new, Collectors.toCollection(ArrayList::new)));
                List<List<Customer>> results = new ArrayList<>();
                list.forEach(employee -> results.add(customerMap.get(employee)));
                return CompletableFuture.supplyAsync(() -> results);
            }
        }));
        // Payment Data Loader
        dataLoaderRegistry.register("paymentDataLoader", DataLoaderFactory.newDataLoader(new BatchLoader<Integer, List<Payment>>() {
            @Override
            public CompletionStage<List<List<Payment>>> load(List<Integer> list) {
                // load all payments by customer ids
                List<Payment> payments = paymentService.getAllByCustomer(list);
                // map payments to customer list
                Map<Integer, List<Payment>> paymentMap = payments.stream().collect(Collectors.groupingBy(Payment::getCustomerNumber, HashMap::new, Collectors.toCollection(ArrayList::new)));
                List<List<Payment>> results = new ArrayList<>();
                list.forEach(customer -> results.add(paymentMap.get(customer)));
                return CompletableFuture.supplyAsync(() -> results);
            }
        }));
        // Order Data Loader
        dataLoaderRegistry.register("orderDataLoader", DataLoaderFactory.newDataLoader(new BatchLoader<Integer, List<Order>>() {
            @Override
            public CompletionStage<List<List<Order>>> load(List<Integer> list) {
                // load all orders by customer ids
                List<Order> orders = orderService.getAllByCustomer(list);
                // map orders to customer list
                Map<Integer, List<Order>> orderMap = orders.stream().collect(Collectors.groupingBy(o -> o.getCustomer().getId(), HashMap::new, Collectors.toCollection(ArrayList::new)));
                List<List<Order>> results = new ArrayList<>();
                list.forEach(customer -> results.add(orderMap.get(customer)));
                return CompletableFuture.supplyAsync(() -> results);
            }
        }));
        // Employees Data Loader
        dataLoaderRegistry.register("employeesDataLoader", DataLoaderFactory.newDataLoader(new BatchLoader<Integer, List<Employee>>() {
            @Override
            public CompletionStage<List<List<Employee>>> load(List<Integer> list) {
                // load all employees by office id
                List<Employee> employees = employeeService.getAllByOffice(list);
                // map employees to office
                Map<Integer, List<Employee>> employeeMap = employees.stream().collect(Collectors.groupingBy(e -> e.getOffice().getId(), HashMap::new, Collectors.toCollection(ArrayList::new)));
                List<List<Employee>> results = new ArrayList<>();
                list.forEach(office -> results.add(employeeMap.get(office)));
                return CompletableFuture.supplyAsync(() -> results);
            }
        }));
        // OrderDetail Data Loader
        dataLoaderRegistry.register("orderDetailsDataLoader", DataLoaderFactory.newDataLoader(new BatchLoader<Integer, List<OrderDetail>>() {
            @Override
            public CompletionStage<List<List<OrderDetail>>> load(List<Integer> list) {
                // load all order details by order
                List<OrderDetail> orderDetails = orderDetailService.getAllByOrders(list);
                // map orderDetails to order id
                Map<Integer, List<OrderDetail>> orderDetailMap = orderDetails.stream().collect(Collectors.groupingBy(OrderDetail::getOrder, HashMap::new, Collectors.toCollection(ArrayList::new)));
                List<List<OrderDetail>> results = new ArrayList<>();
                list.forEach(order -> results.add(orderDetailMap.get(order)));
                return CompletableFuture.supplyAsync(() -> results);
            }
        }));
        // Product Data Loader
        dataLoaderRegistry.register("productDataLoader", DataLoaderFactory.newDataLoader(new BatchLoader<String, List<Product>>() {
            @Override
            public CompletionStage<List<List<Product>>> load(List<String> list) {
                // load all products by orderDetail product ids
                List<Product> products = productService.getByIDs(list);
                // map orders to customer list
                Map<String, List<Product>> orderMap = products.stream().collect(Collectors.groupingBy(Product::getId, HashMap::new, Collectors.toCollection(ArrayList::new)));
                List<List<Product>> results = new ArrayList<>();
                list.forEach(orderDetail -> results.add(orderMap.get(orderDetail)));
                return CompletableFuture.supplyAsync(() -> results);
            }
        }));
        return dataLoaderRegistry;
    }
}
