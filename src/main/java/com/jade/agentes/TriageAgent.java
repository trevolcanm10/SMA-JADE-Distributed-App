package com.jade.agentes;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class TriageAgent extends Agent {
    @Override
    protected void setup() {
        // 1. Registro en el DF de la Plataforma A
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("triage");
        sd.setName("servicio-triage");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) { fe.printStackTrace(); }

        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage msg = myAgent.receive();
                if (msg != null && msg.getPerformative() == ACLMessage.INFORM) {
                    String[] partes = msg.getContent().split(",");
                    if (partes.length >= 2) {
                        String nombre = partes[0].trim();
                        String gravedad = partes[1].trim();
                        
                        System.out.println("[Triage] Procesando y enviando a Plataforma B por IP Directa...");

                        // 2. Definición del Receptor en Plataforma Externa
                        // El nombre DEBE coincidir con el del agente y el -name de la plataforma
                        // 2. Definición del Receptor en Plataforma Externa
                        AID remoteAID = new AID("Asignador@PlataformaGestion", AID.ISGUID);
                                            
                        // 3. Configuración del Transporte Inter-Plataforma (MTP)
                        remoteAID.clearAllAddresses(); 
                        remoteAID.addAddresses("http://172.20.0.3:7778/acc"); 
                                            
                        // 4. Construcción del Mensaje
                        ACLMessage msgAsignador = new ACLMessage(ACLMessage.REQUEST);
                        msgAsignador.addReceiver(remoteAID); // JADE usará la dirección añadida arriba
                        msgAsignador.setContent(nombre + "," + gravedad);

                        // 5. Envío
                        myAgent.send(msgAsignador);
                    }
                } else {
                    block();
                }
            }
        });
    }

    @Override
    protected void takeDown() {
        try { DFService.deregister(this); } catch (FIPAException fe) { fe.printStackTrace(); }
    }
}