package com.jade.agentes;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

public class DoctorAssignerAgent extends Agent {
    protected void setup() {
        // Registro en DF de la Plataforma B
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("asignacion-medica");
        sd.setName("servicio-asignador");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) { fe.printStackTrace(); }

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String[] datos = msg.getContent().split(",");
                    String paciente = datos[0];
                    String nivel = datos[1];
                    String medico = asignar(nivel);

                    // Busca al Doctor en la misma plataforma (B)
                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sdDoctor = new ServiceDescription();
                    sdDoctor.setType("atencion-medica");
                    template.addServices(sdDoctor);

                    try {
                        DFAgentDescription[] result = DFService.search(myAgent, template);
                        if (result.length > 0) {
                            ACLMessage nuevo = new ACLMessage(ACLMessage.INFORM);
                            nuevo.addReceiver(result[0].getName());
                            nuevo.setContent(paciente + "," + medico + "," + nivel);
                            send(nuevo);
                        }
                    } catch (Exception fe) { fe.printStackTrace(); }
                } else { block(); }
            }
        });
    }

    private String asignar(String nivel) {
        if (nivel.equals("infarto") || nivel.equals("dificultad respiratoria")) return "Emergencia";
        if (nivel.equals("fiebre")) return "General";
        return "Consulta";
    }
}