package com.project.demo.logic.entity.email;


/**
 * Plantillas de correo electrónico utilizadas por el sistema.
 */
public class EmailTemplates {

    /**
     * Genera el cuerpo del correo para una contraseña temporal.
     *
     * @param name     nombre del usuario
     * @param email    correo del usuario
     * @param password contraseña temporal
     * @param loginUrl URL de inicio de sesión
     * @return cuerpo del correo
     */
    public static String temporaryPasswordEmail(String name, String email, String password, String loginUrl) {
        return "Hola " + name + ",\n\n" +
                "Se ha generado una contraseña temporal con éxito. Aquí están tus credenciales:\n" +
                "Correo: " + email + "\n" +
                "Contraseña: " + password + "\n\n" +
                "Por favor, cambia tu contraseña al iniciar sesión usando la dirección " + loginUrl + ".\n\n" +
                "Saludos,\nEl equipo de EduSmart";
    }

    /**
     * Genera el cuerpo del correo para una nueva cuenta.
     *
     * @param name     nombre del usuario
     * @param email    correo del usuario
     * @param password contraseña inicial
     * @param loginUrl URL de inicio de sesión
     * @return cuerpo del correo
     */
    public static String newAccountEmail(String name, String email, String password, String loginUrl) {
        return "Hola " + name + ",\n\n" +
                "Tu cuenta ha sido creada con éxito. Aquí están tus credenciales:\n" +
                "Correo: " + email + "\n" +
                "Contraseña: " + password + "\n\n" +
                "Por favor, cambia tu contraseña al iniciar sesión usando la dirección " + loginUrl + ".\n\n" +
                "Saludos,\nEl equipo de EduSmart";
    }

}
