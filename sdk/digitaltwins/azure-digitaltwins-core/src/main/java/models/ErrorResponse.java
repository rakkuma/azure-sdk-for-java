/**
 * Code generated by Microsoft (R) AutoRest Code Generator.
 * Changes may cause incorrect behavior and will be lost if the code is
 * regenerated.
 */

package digitaltwins.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Error response.
 */
public class ErrorResponse {
    /**
     * The error details.
     */
    @JsonProperty(value = "error")
    private Error error;

    /**
     * Get the error details.
     *
     * @return the error value
     */
    public Error error() {
        return this.error;
    }

    /**
     * Set the error details.
     *
     * @param error the error value to set
     * @return the ErrorResponse object itself.
     */
    public ErrorResponse withError(Error error) {
        this.error = error;
        return this;
    }

}
