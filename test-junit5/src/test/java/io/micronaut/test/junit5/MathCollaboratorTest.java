
package io.micronaut.test.junit5;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@MicronautTest
class MathCollaboratorTest {

    @Inject
    MathService mathService;

    @Inject
    @Client("/")
    HttpClient client; // <2>


    @ParameterizedTest
    @CsvSource({"2,4", "3,9"})
    void testComputeNumToSquare(Integer num, Integer square) {

        when( mathService.compute(num) )
            .then(invocation -> Long.valueOf(Math.round(Math.pow(num, 2))).intValue());

        final Integer result = client.toBlocking().retrieve(HttpRequest.GET("/math/compute/" + num), Integer.class); // <3>

        assertEquals(
                square,
                result
        );
        verify(mathService).compute(num); // <4>
    }

    @MockBean(MathServiceImpl.class) // <1>
    MathService mathService() {
        return mock(MathService.class);
    }

}
