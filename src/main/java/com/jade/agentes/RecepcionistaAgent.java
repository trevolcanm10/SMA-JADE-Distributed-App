    package com.jade.agentes;

    import jade.core.AID;
    import jade.core.Agent;
    import jade.lang.acl.ACLMessage;
    public class RecepcionistaAgent extends Agent {

        private String[][] pacientes = {
            {"Juan", "dificultad respiratoria"},
            {"Ana", "fiebre"},
            {"Luis", "dolor cabeza"},
            {"Maria", "infarto"},
            {"Carlos", "fractura"},
            {"Elena", "gripe"},
            {"Pedro", "dolor de estomago"},
            {"Sofia", "quemadura"},
            {"Jorge", "corte profundo"},
            {"Lucia", "nauseas"}
        };

        private int index = 0;
        private AID triageAID;
        private boolean esperandoTriage = false;
        protected void setup() {

            System.out.println(getLocalName() + " iniciado. Esperando a que los demás agentes se registren...");
           
            addBehaviour(new jade.core.behaviours.TickerBehaviour(this, 5000) {

                protected void onTick() {
                    
                    if (index >= pacientes.length) {
                        System.out.println("No hay más pacientes.");
                        myAgent.removeBehaviour(this);
                        return;
                    }

                    String paciente = pacientes[index][0];
                    String sintoma = pacientes[index][1];
                    
                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);

                    try {

                        if (triageAID == null){

                            jade.domain.FIPAAgentManagement.DFAgentDescription template
                                    = new jade.domain.FIPAAgentManagement.DFAgentDescription();

                            jade.domain.FIPAAgentManagement.ServiceDescription sdTriaje
                                    = new jade.domain.FIPAAgentManagement.ServiceDescription();

                            sdTriaje.setType("triaje-medico");
                            template.addServices(sdTriaje);

                            jade.domain.FIPAAgentManagement.DFAgentDescription[] result
                                    = jade.domain.DFService.search(myAgent, template);

                            if (result.length > 0) {

                                triageAID = result[0].getName();
                                esperandoTriage = false;
                                System.out.println("✔ Triage encontrado en DF.");

                            } else {

                                if (!esperandoTriage) {
                                    System.out.println("Esperando que el Triage se registre...");
                                    esperandoTriage = true;
                                }

                                return; // aún no enviamos nada

                            }
                        }
                        // 📩 Enviar mensaje cuando ya existe
                        msg.addReceiver(triageAID);
                        msg.setContent(paciente + "," + sintoma);
                        send(msg);
                        System.out.println("Datos enviados al Triage.");
                        index++;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }