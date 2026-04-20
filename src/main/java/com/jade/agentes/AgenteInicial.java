package com.jade.agentes;

import jade.core.Agent;

public class AgenteInicial extends Agent {

    @Override
    protected void setup() {
        // Mensaje de bienvenida
        System.out.println("----------------------------------------------");
        System.out.println("¡HOLA! Soy el agente: " + getAID().getName());
        System.out.println("Estado: Esperando instrucciones en la plataforma.");
        System.out.println("----------------------------------------------");
    }

    @Override
    protected void takeDown() {
        System.out.println("Agente " + getAID().getName() + " finalizando.");
    }
}