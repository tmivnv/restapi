/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.uglevodov.restapi.entities.FAQEntry;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.exceptions.NotUpdatableException;
import net.uglevodov.restapi.repositories.FAQRepository;
import net.uglevodov.restapi.service.FAQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@Slf4j
public class FAQServiceImpl implements FAQService {

    @Autowired
    private FAQRepository faqRepository;

    @Override
    public FAQEntry save(FAQEntry faqEntry) {
        log.trace("[{}] - Saving faq entry {}", this.getClass().getSimpleName(), faqEntry);
        return faqRepository.save(faqEntry);
    }

    @Override
    public FAQEntry get(long id) throws NotFoundException {
        log.trace("[{}] - Getting faq entry id = ", this.getClass().getSimpleName(), id);

        return faqRepository.findById(id).orElseThrow(() -> new NotFoundException("faq entry id " + id + " not found"));
    }

    @Override
    public void update(FAQEntry faqEntry) throws NotUpdatableException {
        log.trace("[{}] - Updating faq entry {}", this.getClass().getSimpleName(), faqEntry);
        faqRepository.findById(faqEntry.getId()).orElseThrow(() -> new NotFoundException("faq entry id " + faqEntry.getId() + " not found"));
        Assert.notNull(faqEntry, "FAQ entry can not be null");
        faqRepository.save(faqEntry);
    }

    @Override
    public void delete(long id) throws NotFoundException {
        log.trace("[{}] - Deleting faq entry id = {}", this.getClass().getSimpleName(), id);
        FAQEntry faqEntry = faqRepository.findById(id).orElseThrow(() -> new NotFoundException("dish id " + id + " not found"));

        faqRepository.delete(faqEntry);
    }

    @Override
    public Page<FAQEntry> getAll(Pageable pageRequest) {
        return null;
    }

    @Override
    public List<FAQEntry> findAllByCategory(Integer category) {
        return faqRepository.findAllByCategory(category).orElseThrow(()-> new NotFoundException("no entries found"));
    }

    @Override
    public List<FAQEntry> findAllByQuestionOrAnswerContaining(String string) {
        return faqRepository.findAllByAnswerContainingIgnoreCaseOrQuestionContainingIgnoreCase(string, string).orElseThrow(()-> new NotFoundException("no entries found"));
    }
}
