package de.hidin.linktreebackendservice.entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LinkEntryEntryService {
    private final LinkEntryRepository linkRepository;

    @Autowired
    public LinkEntryEntryService(LinkEntryRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public LinkEntry createLinkEntry(String targetUrl, String displayText) {
        LinkEntry link = new LinkEntry();
        link.setTargetUrl(targetUrl);
        link.setDisplayText(displayText);
        return linkRepository.save(link);
    }

    // Add more methods for CRUD operations or custom queries as needed
}
