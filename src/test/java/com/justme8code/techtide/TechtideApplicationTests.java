package com.justme8code.techtide;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ActiveProfiles("dev")
@ContextConfiguration(classes = TechtideApplicationTests.class)
class TechtideApplicationTests {

    @Test
    void contextLoads() {
        // Just Here
    }

}
