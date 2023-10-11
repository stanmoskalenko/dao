package org.example;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;


@RequiredArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class User {
    final String name;
    final String phone;
    Long id;
}
