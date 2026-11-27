package org.secretfriend.config;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.method.HandlerMethod;

import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

@Configuration
public class OpenApiConfig {

    private static final String PROBLEM_DETAIL_SCHEMA_NAME = "ProblemDetail";

    @Bean
    public OpenApiCustomizer globalErrorResponseCustomizer() {

        return openApi -> {

            if (openApi.getComponents() != null) {
                Schema<?> existingSchema = openApi.getComponents().getSchemas().get(PROBLEM_DETAIL_SCHEMA_NAME);

                if (existingSchema == null) {
                    Schema<ProblemDetail> problemDetailSchema = createProblemDetailSchema();
                    openApi.getComponents().addSchemas(PROBLEM_DETAIL_SCHEMA_NAME, problemDetailSchema);
                }
            }
        };
    }

    @Bean
    public OperationCustomizer customizeGlobalResponses() {

        return (Operation operation, HandlerMethod _) -> {

            ApiResponses responses = operation.getResponses();

            if (responses == null) {
                responses = new ApiResponses();
                operation.setResponses(responses);

            }
            else {
                responses.forEach((key, value) -> {
                    HttpStatus status = HttpStatus.valueOf(Integer.parseInt(key));

                    if (status.isError()) {
                        Schema<?> customSchema = buildCustomSchema(status.value());
                        value.setContent(new Content()
                                .addMediaType(
                                        APPLICATION_PROBLEM_JSON_VALUE,
                                        new MediaType().schema(customSchema)
                                ));
                    }
                });
            }

            // Docs default problem responses, so it doesn't have to be documented in the endpoint
            addProblemApiResponse(responses, 400, "Invalid Request");
            addProblemApiResponse(responses, 401, "Unauthorized");
            addProblemApiResponse(responses, 500, "Unexpected error");

            return operation;
        };
    }

    private void addProblemApiResponse(ApiResponses responses, int status, String description) {

        String statusAsText = String.valueOf(status);
        if (!responses.containsKey(statusAsText)) {

            Schema<?> customSchema = buildCustomSchema(status);
            ApiResponse response = new ApiResponse()
                    .description(description)
                    .content(new Content()
                            .addMediaType(
                                    APPLICATION_PROBLEM_JSON_VALUE,
                                    new MediaType().schema(customSchema)));

            responses.addApiResponse(statusAsText, response);
        }
    }

    private Schema<?> buildCustomSchema(int status) {

        ResolvedSchema resolvedSchema = ModelConverters.getInstance().resolveAsResolvedSchema(
                new AnnotatedType(ProblemDetail.class)
        );

        Schema<?> customSchema = resolvedSchema.schema;

        customSchema.getProperties().remove("type");
        customSchema.getProperties().remove("properties");

        if (customSchema.getProperties().containsKey("status")) {
            Schema<?> statusProperty = customSchema.getProperties().get("instance");
            statusProperty.setExample(status);
        }

        return customSchema;
    }

    private Schema<ProblemDetail> createProblemDetailSchema() {

        Schema<ProblemDetail> problemDetailSchema = new Schema<>();
        problemDetailSchema.setName(PROBLEM_DETAIL_SCHEMA_NAME);
        problemDetailSchema.setType("object");
        problemDetailSchema.setDescription("Default RFC 7807 error representation");

        return problemDetailSchema;
    }
}
