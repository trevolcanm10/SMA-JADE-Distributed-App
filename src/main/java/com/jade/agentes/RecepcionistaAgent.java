    package com.jade.agentes;

    import jade.core.AID;
    import jade.core.Agent;
    import jade.lang.acl.ACLMessage;
    public class RecepcionistaAgent extends Agent {

        private String[][] pacientes = {
            {"Juan", "dificultad respiratoria"},
            {"Ana", "fiebre"},
            {"Luis", "dolor cabeza"}
        };

        private int index = 0;
        private AID triageAID;
        protected void setup() {

            System.out.println(getLocalName() + " iniciado. Esperando a que los demás agentes se registren...");
            try {

                jade.domain.FIPAAgentManagement.DFAgentDescription template
                        = new jade.domain.FIPAAgentManagement.DFAgentDescription();

                jade.domain.FIPAAgentManagement.ServiceDescription sdTriaje
                        = new jade.domain.FIPAAgentManagement.ServiceDescription();

                sdTriaje.setType("triaje-medico");
                template.addServices(sdTriaje);

                jade.domain.FIPAAgentManagement.DFAgentDescription[] result
                        = jade.domain.DFService.search(this, template);

                if (result.length > 0) {
                    triageAID = result[0].getName();
                    System.out.println("Triage guardado en memoria.");
                } else {
                    System.out.println("Triage no encontrado en DF al inicio.");
                }

            } catch (jade.domain.FIPAException fe) {
                fe.printStackTrace();
            }
            
            addBehaviour(new jade.core.behaviours.TickerBehaviour(this, 5000) {

                protected void onTick() {
                    
                    if (index >= pacientes.length) {
                        System.out.println("No hay más pacientes.");
                        myAgent.removeBehaviour(this);
                        return;
                    }

                    String paciente = pacientes[index][0];
                    String sintoma = pacientes[index][1];
                    index++;

                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);

                    try {

                        if (triageAID != null) {

                            msg.addReceiver(triageAID);
                            msg.setContent(paciente + "," + sintoma);

                            send(msg);

                            System.out.println("¡Triage encontrado (cache)!");
                            System.out.println("Datos enviados al Triage.");

                        } else {

                            System.out.println("Triage no disponible en memoria.");
                            System.out.println("Mensaje NO enviado (evita errores).");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }