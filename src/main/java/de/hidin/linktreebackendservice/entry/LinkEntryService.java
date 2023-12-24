package de.hidin.linktreebackendservice.entry;

import de.hidin.linktreebackendservice.collection.LinkCollectionRepository;
import de.hidin.linktreebackendservice.entity.LinkCollection;
import de.hidin.linktreebackendservice.entity.LinkEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinkEntryService {
    private final LinkEntryRepository linkRepository;
    private final LinkCollectionRepository collectionRepository;

    @Autowired
    public LinkEntryService(LinkEntryRepository linkRepository, LinkCollectionRepository linkCollectionRepository) {
        this.linkRepository = linkRepository;
        this.collectionRepository = linkCollectionRepository;
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

    public Page<LinkEntry> searchLinkEntries(SearchCriteria searchCriteria, Pageable pageable) {
        return collectionRepository.findByDisplayTextOrTargetUrl(searchCriteria.getSlug(), searchCriteria.getSearchTerm(), pageable);
    }

    public boolean deleteLinkEntry(Long id) {
        if (linkRepository.existsById(id)) {
            try {
                linkRepository.deleteById(id);
                return true;
            } catch (Exception e) {
                // Log the exception and handle it appropriately
                return false;
            }
        } else {
            return false;
        }
    }

    public LinkEntry updateLinkEntry(Long id, String targetUrl, String displayText) {
        return linkRepository.findById(id)
                .map(linkEntry -> {
                    linkEntry.setTargetUrl(targetUrl);
                    linkEntry.setDisplayText(displayText);
                    return linkRepository.save(linkEntry);
                })
                .orElse(null);
    }
}
