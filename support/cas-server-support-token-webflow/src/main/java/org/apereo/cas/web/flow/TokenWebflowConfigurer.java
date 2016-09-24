package org.apereo.cas.web.flow;

import org.springframework.webflow.engine.ActionState;
import org.springframework.webflow.engine.Flow;

/**
 * The {@link TokenWebflowConfigurer} is responsible for
 * adjusting the CAS webflow context for token authentication support integration.
 *
 * @author Misagh Moayyed
 * @since 4.2
 */

public class TokenWebflowConfigurer extends AbstractCasWebflowConfigurer {

    @Override
    protected void doInitialize() throws Exception {
        final Flow flow = getLoginFlow();
        final ActionState actionState = createActionState(flow, "tokenAuthenticationCheck",
                createEvaluateAction("tokenAuthenticationAction"));
        actionState.getTransitionSet().add(createTransition(CasWebflowConstants.TRANSITION_ID_SUCCESS,
                CasWebflowConstants.TRANSITION_ID_SEND_TICKET_GRANTING_TICKET));
        actionState.getExitActionList().add(createEvaluateAction("clearWebflowCredentialsAction"));
        registerMultifactorProvidersStateTransitionsIntoWebflow(actionState);
        createStateDefaultTransition(actionState, getStartState(flow).getId());
        setStartState(flow, actionState);
    }
}
