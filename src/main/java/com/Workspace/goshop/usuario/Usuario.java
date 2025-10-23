package com.Workspace.goshop.usuario;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Campo é obrigatório")
    private String nomeCompleto;
    @NotBlank
    private LocalDate dataNascimento;
    @CPF
    private String cpf;
    @NotBlank
    @Email(message = "Campo é obrigatório")
    private String email;
    @NotBlank(message = "Campo é obrigatório")
    private String senha;
}
