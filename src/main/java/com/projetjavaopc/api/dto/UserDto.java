package com.projetjavaopc.api.dto;

import com.projetjavaopc.api.models.Users;

public class UserDto {
    private String email;
    private String name;
    private String password;

    // Les constructeurs, les getters et les setters sont omis pour la brièveté

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Ajouter une méthode de conversion d'un objet User en UserDto
    public static UserDto from(Users user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setPassword(user.getPassword());
        return userDto;
    }
}
