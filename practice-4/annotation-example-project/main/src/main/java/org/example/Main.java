package org.example;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        Person person = new Person();
        person.setName("John");
        person.setEmail("john_smith@example.com");

        PersonMapper mapper = new PersonMapper();
        PersonDto dto = mapper.toDto(person);
        System.out.println(dto);
        EmailValidator.validate(person);
    }
}
