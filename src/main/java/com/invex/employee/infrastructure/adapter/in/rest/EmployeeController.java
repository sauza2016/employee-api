package com.invex.employee.infrastructure.adapter.in.rest;

import com.invex.employee.application.port.in.EmployeeUseCase;
import com.invex.employee.domain.model.Employee;
import com.invex.employee.infrastructure.adapter.in.rest.dto.EmployeeCreateRequest;
import com.invex.employee.infrastructure.adapter.in.rest.dto.EmployeeResponse;
import com.invex.employee.infrastructure.adapter.in.rest.dto.EmployeeUpdateRequest;
import com.invex.employee.infrastructure.adapter.in.rest.dto.UpdateEmployeeCommand;
import com.invex.employee.infrastructure.adapter.in.rest.exception.ErrorResponse;
import com.invex.employee.infrastructure.adapter.in.rest.mapper.EmployeeRestMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import java.util.List;
import java.util.stream.Collectors;

@Tag(
        name = "Employees",
        description = "Employee management operations"
)
@Validated
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeUseCase employeeUseCase;

    public EmployeeController(EmployeeUseCase employeeUseCase) {
        this.employeeUseCase = employeeUseCase;
    }

    @Operation(
            summary = "Get all employees",
            description = "Returns all registered employees"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Employees retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(
                                            implementation = EmployeeResponse.class
                                    )
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> findAll() {

        List<EmployeeResponse> response = employeeUseCase.findAll()
                .stream()
                .map(EmployeeRestMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get employee by id",
            description = "Returns an employee using its unique identifier"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = EmployeeResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Employee not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> findById(
            @Parameter(
                    description = "Unique employee identifier",
                    example = "1",
                    required = true
            )
            @PathVariable Long id) {

        Employee employee = employeeUseCase.findById(id);

        return ResponseEntity.ok(
                EmployeeRestMapper.toResponse(employee)
        );
    }

    @Operation(
            summary = "Create employees",
            description = "Creates one or multiple employees in the same request"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Employees created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(
                                            implementation = EmployeeResponse.class
                                    )
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or validation error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<List<EmployeeResponse>> create(
            @NotEmpty(message = "Employee list must not be empty")
            @Valid
            @RequestBody List<@Valid EmployeeCreateRequest> requests) {
        List<Employee> employees = requests.stream()
                .map(EmployeeRestMapper::toDomain)
                .collect(Collectors.toList());

        List<EmployeeResponse> response = employeeUseCase.create(employees)
                .stream()
                .map(EmployeeRestMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Update employee",
            description = "Updates all or some employee fields"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = EmployeeResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or validation error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Employee not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> update(
            @Parameter(
                    description = "Unique identifier of the employee to update",
                    example = "1",
                    required = true
            )
            @PathVariable Long id,

            @Valid
            @RequestBody EmployeeUpdateRequest request) {

        UpdateEmployeeCommand command =
                EmployeeRestMapper.toCommand(request);

        Employee updated =
                employeeUseCase.update(id, command);

        return ResponseEntity.ok(
                EmployeeRestMapper.toResponse(updated)
        );
    }

    @Operation(
            summary = "Delete employee",
            description = "Deletes an employee using its unique identifier"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Employee deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Employee not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(
                    description = "Unique identifier of the employee to delete",
                    example = "1",
                    required = true
            )
            @PathVariable Long id) {

        employeeUseCase.delete(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Search employees by name",
            description = "Performs a partial employee name search"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Search completed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(
                                            implementation = EmployeeResponse.class
                                    )
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Search name is invalid",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            )
    })
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeResponse>> searchByName(
            @Parameter(
                    description = "Partial employee name to search for",
                    example = "Joh",
                    required = true
            )
            @NotBlank(message = "Name must not be blank")
            @RequestParam String name) {

        List<EmployeeResponse> response = employeeUseCase.searchByName(name)
                .stream()
                .map(EmployeeRestMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}