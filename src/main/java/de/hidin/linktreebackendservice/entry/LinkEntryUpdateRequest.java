package de.hidin.linktreebackendservice.entry;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinkEntryUpdateRequest {
    private String targetUrl;
    private String displayText;
}
