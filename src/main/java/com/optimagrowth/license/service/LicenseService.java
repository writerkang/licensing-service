package com.optimagrowth.license.service;

import com.optimagrowth.license.model.License;
import com.optimagrowth.license.repository.LicenseRepository;
import java.util.Locale;
import java.util.Objects;
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

    public License getLicense(String licenseId, String organizationId) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        Objects.requireNonNull(license);

        return license.withComment("H2 database");
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
