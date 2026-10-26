package com.yugal.store.commons;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Tag(name = "Health", description = "Service health checks")
public class HealthController {

    @GetMapping("/health")
    @Operation(summary = "Check service health")
    @ApiResponse(responseCode = "200", description = "Service is healthy",
            content = @Content(schema = @Schema(type = "object", example = "{\"status\":\"UP\"}"),
                    examples = @ExampleObject(value = "{\"status\":\"UP\"}")))
    @SecurityRequirements
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }
}