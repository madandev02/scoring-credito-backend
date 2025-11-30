package com.madandev.creditscoring.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RutValidator implements ConstraintValidator<ValidRut, String> {

    @Override
    public void initialize(ValidRut constraintAnnotation) {
        // Inicialización si es necesaria
    }

    @Override
    public boolean isValid(String rut, ConstraintValidatorContext context) {
        // Si rut == null -> return false
        if (rut == null || rut.trim().isEmpty()) {
            return false;
        }

        try {
            // Eliminar puntos y guión del rut, convertir mayúscula
            String rutLimpio = rut.replace(".", "").replace("-", "").toLowerCase();

            // Verificar que tenga al menos 2 caracteres (cuerpo + DV)
            if (rutLimpio.length() < 2) {
                return false;
            }

            // Extraer el último caracter -> es el DV
            String dvIngresado = rutLimpio.substring(rutLimpio.length() - 1);

            // Extraer el resto -> cuerpo del RUT
            String cuerpoStr = rutLimpio.substring(0, rutLimpio.length() - 1);

            // Validar que el cuerpo sea numérico
            if (!cuerpoStr.matches("\\d+")) {
                return false;
            }

            // Manejar Dvs inválidos
            // Si el DV ingresado NO es "0-9" ni "K", entonces retorna false directamente
            if (!dvIngresado.matches("[0-9kK]")) {
                return false;
            }

            // Convertimos cuerpo a número
            int cuerpo = Integer.parseInt(cuerpoStr);

            // Implementar la fórmula del dígito verificador
            int suma = 0;
            int multiplicador = 2;

            // Recorrer el cuerpo de derecha a izquierda
            for (int i = cuerpoStr.length() - 1; i >= 0; i--) {
                int digito = Character.getNumericValue(cuerpoStr.charAt(i));
                suma += digito * multiplicador;
                multiplicador++;
                if (multiplicador > 7) {
                    multiplicador = 2;
                }
            }

            // Calcular DV esperado
            int resto = suma % 11;
            int dvCalculadoNum = 11 - resto;

            // Trducir resultado según fórmula oficial
            String dvCalculado;
            if (dvCalculadoNum == 11) {
                dvCalculado = "0";
            } else if (dvCalculadoNum == 10) {
                dvCalculado = "K";
            } else {
                dvCalculado = String.valueOf(dvCalculadoNum);
            }

            // Comparar DV calculado con el Dv ingresado
            return dvCalculado.equals(dvIngresado);

        } catch (Exception e) {
            // Cauqluier error en el proceso -> Rut invalido
            return false;
        }
    }
}

