package ua.dimichfast.authenticationserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Developer {
    Long id;
    String name;
    String lastName;
}
