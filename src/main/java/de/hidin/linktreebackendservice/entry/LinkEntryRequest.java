package de.hidin.linktreebackendservice.entry;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class LinkEntryRequest {
    private String targetUrl;
    private String displayText;

}
