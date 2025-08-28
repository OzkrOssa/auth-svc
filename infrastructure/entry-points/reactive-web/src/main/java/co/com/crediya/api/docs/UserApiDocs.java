package co.com.crediya.api.docs;

import co.com.crediya.api.Handler;
import co.com.crediya.api.dto.ResponseDto;
import co.com.crediya.api.dto.UserRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@Tag(name = "User API", description = "Endpoints for user management")
public class UserApiDocs {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/users",
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "registerUser",
                    operation = @Operation(
                            operationId = "registerUser",
                            summary = "Register a new user",
                            description = "Registers a new user in the system",
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "User creation payload",
                                    content = @Content(schema = @Schema(implementation = UserRequestDto.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "User created successfully",
                                            content = @Content(
                                                    schema = @Schema(implementation = ResponseDto.class),
                                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                            value = "{\"success\":true,\"message\":\"User created successfully\",\"data\":{\"id\":1,\"firstName\":\"Darion\",\"lastName\":\"Hagenes\",\"email\":\"Madelyn_Halvorson@hotmail.com\",\"document\":\"28420347\",\"phone\":\"996-919-5740\",\"baseSalary\":1000,\"baseSalaryValid\":true},\"errors\":null}"
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Validation error",
                                            content = @Content(
                                                    schema = @Schema(implementation = ResponseDto.class),
                                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                            value = "{\"success\":false,\"message\":\"Validation error\",\"data\":null,\"errors\":[\"firstName is required\",\"base salary must be greater than 0\"]}"
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Unexpected server error",
                                            content = @Content(
                                                    schema = @Schema(implementation = ResponseDto.class),
                                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                            value = "{\"success\":false,\"message\":\"Unexpected server error\",\"data\":null,\"errors\":[\"string\",\"string\"]}"
                                                    )
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/users/{id}",
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "getUser",
                    operation = @Operation(
                            operationId = "getUser",
                            summary = "Retrieve a user by ID",
                            description = "Retrieves a single user from the system using its unique identifier",
                            parameters = {
                                    @io.swagger.v3.oas.annotations.Parameter(
                                            name = "id",
                                            description = "Unique identifier of the user",
                                            required = true,
                                            in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "User retrieved successfully",
                                            content = @Content(
                                                    schema = @Schema(implementation = ResponseDto.class),
                                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                            value = "{\"success\":true,\"message\":\"User retrieved successfully\",\"data\":{\"id\":1,\"firstName\":\"Darion\",\"lastName\":\"Hagenes\",\"email\":\"Madelyn_Halvorson@hotmail.com\",\"document\":\"28420347\",\"phone\":\"996-919-5740\",\"baseSalary\":1000,\"baseSalaryValid\":true},\"errors\":null}"

                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "User not found",
                                            content = @Content(
                                                    schema = @Schema(implementation = ResponseDto.class),
                                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                            value = "{ \"success\": false, \"message\": \"User not found\", \"data\": null, \"errors\": [\"No user found with the given ID\"] }"
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Unexpected server error",
                                            content = @Content(
                                                    schema = @Schema(implementation = ResponseDto.class),
                                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                            value = "{ \"success\": false, \"message\": \"Unexpected error\", \"data\": null, \"errors\": [\"Internal server error\"] }"
                                                    )
                                            )
                                    )

                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/users",
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "getUsers",
                    operation = @Operation(
                            operationId = "getUsers",
                            summary = "Retrieve all users",
                            description = "Returns a list of all users in the system",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Users retrieved successfully",
                                            content = @Content(
                                                    schema = @Schema(implementation = ResponseDto.class),
                                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                            value = "{\"success\":true,\"message\":\"Users retrieved successfully\",\"data\":[{\"id\":1,\"firstName\":\"Darion\",\"lastName\":\"Hagenes\",\"email\":\"Madelyn_Halvorson@hotmail.com\",\"document\":\"28420347\",\"phone\":\"996-919-5740\",\"baseSalary\":1000,\"baseSalaryValid\":true},{\"id\":2,\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\",\"document\":\"12345678\",\"phone\":\"123-456-7890\",\"baseSalary\":2000,\"baseSalaryValid\":true}],\"errors\":null}"
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Unexpected server error",
                                            content = @Content(
                                                    schema = @Schema(implementation = ResponseDto.class)
                                            )
                                    )
                            }
                    )
            )

    })
    public RouterFunction<ServerResponse> userApiDoc(Handler handler) {
        // Este router nunca se usa en la app, solo sirve para SpringDoc
        return RouterFunctions.route()
                .GET("/api/v1/users", handler::getUsers)
                .GET("/api/v1/users/{id}", handler::getUser)
                .POST("/api/v1/users", handler::registerUser)
                .build();
    }
}

