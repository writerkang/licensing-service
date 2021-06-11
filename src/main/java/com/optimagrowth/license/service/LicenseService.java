package com.optimagrowth.license.service;

import com.optimagrowth.license.controller.LicenseController;
import com.optimagrowth.license.model.License;
import com.optimagrowth.license.repository.LicenseRepository;
import com.optimagrowth.license.utils.UserContextHolder;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.bulkhead.annotation.Bulkhead.Type;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class LicenseService {

    @Autowired
    MessageSource messages;

    @Autowired
    private LicenseRepository licenseRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(LicenseController.class);

    public License getLicense(String licenseId, String organizationId) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        Objects.requireNonNull(license);

        return license.withComment("H2 database");
    }

//    @RateLimiter(name = "licenseService", fallbackMethod = "buildFallbackLicenseList")
//    @Retry(name = "retryLicenseService", fallbackMethod = "buildFallbackLicenseList")
//    @Bulkhead(name = "bulkHeadLicenseService", fallbackMethod = "buildFallbackLicenseList", type = Type.THREADPOOL)
//    @CircuitBreaker(name = "licenseService", fallbackMethod = "buildFallbackLicenseList")
    public List<License> getLicensesByOrganization(String organizationId) throws TimeoutException {
        LOGGER.debug("getLicensesByOrganization Correlation id: {}",
            UserContextHolder.getContext().getCorrelationId());

        randomlyRunLong();

//        return licenseRepository.findByOrganizationId(organizationId);
        return Collections.emptyList();
    }

    private List<License> buildFallbackLicenseList(String organizationId, Throwable t) {
        List<License> fallbackList = new ArrayList<>();
        License license = new License();
        license.setLicenseId("453535-123123");
        license.setOrganizationId(organizationId);
        license.setProductName("SOrry no licencing information currently available");
        fallbackList.add(license);

        return fallbackList;
    }

    private void randomlyRunLong() throws TimeoutException {
        Random random = new Random();
        int randomNum = random.nextInt(3) + 1;
        if (randomNum == 3) {
            sleep();
        }
    }

    private void sleep() throws TimeoutException {
        try {
            Thread.sleep(5000);
            throw new TimeoutException();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String createLicense(License license, String organizationId, Locale locale) {
        String responseMessage = null;
        if (!ObjectUtils.isEmpty(license)) {
            license.setOrganizationId(organizationId);
            responseMessage = String.format(messages.getMessage("license.create.message", null, locale),
            license.toString());
        }

        return responseMessage;
    }

    public String deleteLicense(String licenseId, String organizationId) {
        String responseMessage = null;
        responseMessage = String.format("Deleting license with id %s for the organization %s",
            licenseId, organizationId);

        return responseMessage;
    }

    public String updateLicense(License license, String organizationId) {
        String responseMessage = null;
        if (!ObjectUtils.isEmpty(license)) {
            license.setOrganizationId(organizationId);
            responseMessage = String.format(messages.getMessage("license.update.message", null, null),
                license.toString());
        }

        return responseMessage;
    }

    public License getLicense(String licenseId, String organiztionId, String clientType) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organiztionId, licenseId);

        Objects.requireNonNull(license);

        return license.withComment("organization");
    }
}
