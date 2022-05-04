package de.bail.classicmodels.resource.graphql;

import de.bail.classicmodels.model.enities.Customer;
import de.bail.classicmodels.resource.graphql.datafetcher.*;
import de.bail.classicmodels.service.*;
import graphql.TypeResolutionEnvironment;
import graphql.kickstart.servlet.GraphQLConfiguration;
import graphql.kickstart.servlet.GraphQLHttpServlet;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.schema.TypeResolver;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import java.io.File;
import java.util.Objects;

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
    private PaymentService paymentService;

    @Inject
    private ProductService productService;

    @Inject
    private ProductLineService productLineService;

    @Override
    protected GraphQLConfiguration getConfiguration() {
        return GraphQLConfiguration.with(createSchema()).build();
    }

    private GraphQLSchema createSchema() {
        SchemaParser schemaParser = new SchemaParser();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        File schemaFile = new File(Objects.requireNonNull(GraphQLServlet.class.getClassLoader().getResource("schema.graphqls")).getFile());
        TypeDefinitionRegistry typeRegistry = schemaParser.parse(schemaFile);
        RuntimeWiring wiring = buildRuntimeWiring();
        return schemaGenerator.makeExecutableSchema(typeRegistry, wiring);
    }

    private RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                //.scalar(CustomScalar)
                // this uses builder function lambda syntax
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
                .type("Contact", typeWiring -> typeWiring
                        .typeResolver(new TypeResolver() {
                            @Override
                            public GraphQLObjectType getType(TypeResolutionEnvironment env) {
                                Object javaObject = env.getObject();
                                if (javaObject instanceof Customer) {
                                    return env.getSchema().getObjectType("Customer");
                                } else {
                                    return env.getSchema().getObjectType("Employee");
                                }
                            }
                        })

                )
//                .type("Human", typeWiring -> typeWiring
//                        .dataFetcher("friends", StarWarsData.getFriendsDataFetcher())
//                )
//                // you can use builder syntax if you don't like the lambda syntax
//                .type("Droid", typeWiring -> typeWiring
//                        .dataFetcher("friends", StarWarsData.getFriendsDataFetcher())
//                )
//                // or full builder syntax if that takes your fancy
//                .type(
//                        newTypeWiring("Character")
//                                .typeResolver(StarWarsData.getCharacterTypeResolver())
//                                .build()
//                )
                .build();
    }
}
