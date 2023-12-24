package de.hidin.linktreebackendservice.collection;

import de.hidin.linktreebackendservice.entity.LinkCollection;
import de.hidin.linktreebackendservice.entity.LinkEntry;
import de.hidin.linktreebackendservice.entry.LinkEntryRepository;
import de.hidin.linktreebackendservice.entry.LinkEntryRequest;
import de.hidin.linktreebackendservice.entry.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LinkCollectionService {
    private final LinkCollectionRepository collectionRepository;
    private final LinkEntryRepository linkEntryRepository;

    @Autowired
    public LinkCollectionService(LinkCollectionRepository collectionRepository, LinkEntryRepository linkEntryRepository) {
        this.collectionRepository = collectionRepository;
        this.linkEntryRepository = linkEntryRepository;
    }

    public LinkCollection createLinkCollection(String slug) {
        LinkCollection collection = new LinkCollection();
        collection.setSlug(slug);
        return collectionRepository.save(collection);
    }

    public LinkCollection getLinkCollectionBySlug(String slug) {
        return collectionRepository.findBySlug(slug).orElse(null);
    }

    public boolean slugExists(String slug) {
        return getLinkCollectionBySlug(slug) != null;
    }

    public Page<LinkCollection> getAll(Pageable pageable) {
        return collectionRepository.findAll(pageable);
    }

    public Page<LinkCollection> getBySlugStartsWith(String slugPart, Pageable pageable) {
        return collectionRepository.findBySlugStartsWith(slugPart, pageable);
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (!collectionRepository.existsById(id)) {
            return false;
        }

        collectionRepository.deleteById(id);
        return !collectionRepository.existsById(id);
    }

    public LinkEntry addLinkToCollection(String slug, LinkEntryRequest linkEntryRequest) {
        LinkCollection linkCollection = collectionRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("LinkCollection not found"));

        LinkEntry linkEntry = new LinkEntry();
        linkEntry.setTargetUrl(linkEntryRequest.getTargetUrl());
        linkEntry.setDisplayText(linkEntryRequest.getDisplayText());

        linkEntry = linkEntryRepository.save(linkEntry);

        linkCollection.getEntries().add(linkEntry);
        collectionRepository.save(linkCollection);

        return linkEntry;
    }

    public Page<LinkEntry> getAllLinksForCollection(String slug, Pageable pageable) {
        Optional<LinkCollection> collectionOpt = collectionRepository.findBySlug(slug);
        if (!collectionOpt.isPresent()) {
            throw new RuntimeException("LinkCollection with slug '" + slug + "' not found");
        }

        List<LinkEntry> entries = new ArrayList<>(collectionOpt.get().getEntries());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), entries.size());
        return new PageImpl<>(entries.subList(start, end), pageable, entries.size());
    }

}
