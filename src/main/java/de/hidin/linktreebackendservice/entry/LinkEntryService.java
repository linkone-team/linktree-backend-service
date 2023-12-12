package de.hidin.linktreebackendservice.entry;

import de.hidin.linktreebackendservice.entity.LinkEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinkEntryService {
    private final LinkEntryRepository linkRepository;

    @Autowired
    public LinkEntryService(LinkEntryRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public LinkEntry createLinkEntry(String targetUrl, String displayText) {
        LinkEntry link = new LinkEntry();
        link.setTargetUrl(targetUrl);
        link.setDisplayText(displayText);
        return linkRepository.save(link);
    }

    public LinkEntry getLinkEntryById(Long id) {
        return linkRepository.findById(id).orElse(null);
    }


    public List<LinkEntry> getAllLinkEntires() {
        return linkRepository.findAll();
    }

    // Add more methods for CRUD operations or custom queries as needed
}
