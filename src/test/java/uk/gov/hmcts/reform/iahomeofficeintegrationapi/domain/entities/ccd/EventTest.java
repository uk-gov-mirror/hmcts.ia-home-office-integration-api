package uk.gov.hmcts.reform.iahomeofficeintegrationapi.domain.entities.ccd;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class EventTest {

    @Test
    public void has_correct_values() {
        assertEquals("submitAppeal", Event.SUBMIT_APPEAL.toString());
        assertEquals("requestRespondentEvidence", Event.REQUEST_RESPONDENT_EVIDENCE.toString());
        assertEquals("unknown", Event.UNKNOWN.toString());
    }

    @Test
    public void if_this_test_fails_it_is_because_it_needs_updating_with_your_changes() {
        assertEquals(3, Event.values().length);
    }
}
