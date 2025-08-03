package com.project.demo.logic.entity.email;


public class EmailTemplates {

    public static String temporaryPasswordEmail(String name, String email, String password, String loginUrl) {
        return "Hola " + name + ",\n\n" +
                "Se ha generado una contraseña temporal con éxito. Aquí están tus credenciales:\n" +
                "Correo: " + email + "\n" +
                "Contraseña: " + password + "\n\n" +
                "Por favor, cambia tu contraseña al iniciar sesión usando la dirección " + loginUrl + ".\n\n" +
                "Saludos,\nEl equipo de EduSmart";
    }

    public static String newAccountEmail(String name, String email, String password, String loginUrl) {
        return "Hola " + name + ",\n\n" +
                "Tu cuenta ha sido creada con éxito. Aquí están tus credenciales:\n" +
                "Correo: " + email + "\n" +
                "Contraseña: " + password + "\n\n" +
                "Por favor, cambia tu contraseña al iniciar sesión usando la dirección " + loginUrl + ".\n\n" +
                "Saludos,\nEl equipo de EduSmart";
    }

}
