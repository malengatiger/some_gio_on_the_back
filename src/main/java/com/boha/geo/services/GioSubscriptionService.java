package com.boha.geo.services;

import com.boha.geo.models.GioSubscription;
import com.boha.geo.monitor.data.Country;
import com.boha.geo.monitor.data.Organization;
import com.boha.geo.monitor.data.Pricing;
import com.boha.geo.monitor.services.ListService;
import com.boha.geo.repos.GioSubscriptionRepository;
import com.boha.geo.repos.OrganizationRepository;
import com.boha.geo.repos.PricingRepository;
import com.boha.geo.util.Constants;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GioSubscriptionService {

    private final GioSubscriptionRepository subscriptionRepository;
    private final PricingRepository pricingRepository;
    private final ListService listService;
    private final OrganizationRepository organizationRepository;

    public GioSubscriptionService(GioSubscriptionRepository subscriptionRepository,
                                  PricingRepository pricingRepository, ListService listService, OrganizationRepository organizationRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.pricingRepository = pricingRepository;
        this.listService = listService;
        this.organizationRepository = organizationRepository;
    }

    public Pricing addPricing(Pricing pricing) {
        return pricingRepository.insert(pricing);
    }

    public List<Pricing> getPricing(String countryId) {
        return pricingRepository.findByCountryId(countryId);
    }

    public void generateFreeSubscriptions() {
        List<Organization> orgs = organizationRepository.findAll();
        for (Organization org : orgs) {
            GioSubscription gs = new GioSubscription();
            gs.setSubscriptionId(UUID.randomUUID().toString());
            gs.setSubscriptionType(Constants.FREE_SUBSCRIPTION);
            gs.setDate(DateTime.now().toDateTimeISO().toString());
            gs.setActive(0);
            gs.setOrganizationId(org.getOrganizationId());
            gs.setOrganizationName(org.getName());
            subscriptionRepository.insert(gs);
        }
    }

    public List<Pricing> generateTestPricing() {
        List<com.boha.geo.monitor.data.mcountry.Country> countries = listService.getCountries();
        for (com.boha.geo.monitor.data.mcountry.Country country : countries) {
            switch (country.getName()) {
                case "South Africa":
                    writePricing(country, 100.00, 899);
                    break;
                case "Zimbabwe":
                    writePricing(country, 1000.00, 9000);
                    break;
                case "Namibia":
                    writePricing(country, 100.00, 800.00);
                    break;
                case "Botswana":
                    writePricing(country, 200.00, 1999.00);
                    break;

                default:
                    writePricing(country, 100.00, 900.00);
                    break;

            }
        }
        generateFreeSubscriptions();
        return pricingRepository.findAll();
    }

    private Pricing writePricing(com.boha.geo.monitor.data.mcountry.Country country, double monthly, double annual) {
        Pricing p = new Pricing();
        p.setCountryId(country.getCountryId());
        p.setAnnualPrice(annual);
        p.setMonthlyPrice(monthly);
        p.setDate(DateTime.now().toDateTimeISO().toString());
        p.setCountryName(country.getName());

        Pricing mp = pricingRepository.insert(p);
        return mp;
    }

    public GioSubscription addSubscription(GioSubscription subscription) throws Exception {
        return subscriptionRepository.insert(subscription);
    }

    public GioSubscription updateSubscription(GioSubscription subscription) throws Exception {

        List<GioSubscription> subscriptions = subscriptionRepository.findByOrganizationId(
                subscription.getOrganizationId());
        if (subscriptions.isEmpty()) {
            throw new Exception("Subscription not found");
        }
        GioSubscription gioSubscription = subscriptions.get(0);
        gioSubscription.setSubscriptionType(subscription.getSubscriptionType());
        gioSubscription.setUpdated(DateTime.now().toDateTimeISO().toString());
        gioSubscription.setIntUpdated(DateTime.now().getMillis());
        return subscriptionRepository.save(gioSubscription);

    }

    public List<GioSubscription> getSubscriptions() throws Exception {
        return subscriptionRepository.findAll();

    }

    public boolean isSubscriptionValid(String organizationId) throws Exception {
        List<GioSubscription> subscriptions = subscriptionRepository.findByOrganizationId(organizationId);
        if (subscriptions.isEmpty()) {
            return false;
        }
        GioSubscription subscription = subscriptions.get(0);
        if (subscription.getActive() > 0) {
            return false;
        }
        return checkSubscriptionValid(subscription);
    }

    private boolean checkSubscriptionValid(GioSubscription subscription) throws Exception {

        boolean isValid = false;
        switch (subscription.getSubscriptionType()) {
            case Constants.FREE_SUBSCRIPTION -> {
                isValid = isFreeSubscriptionValid(subscription);
            }
            case Constants.MONTHLY_SUBSCRIPTION -> {
                isValid = isMonthlySubscriptionValid(subscription);
            }
            case Constants.ANNUAL_SUBSCRIPTION -> {
                isValid = isAnnualSubscriptionValid(subscription);
            }
            case Constants.CORPORATE_SUBSCRIPTION -> {
                isValid = isCorporateSubscriptionValid(subscription);
            }

        }
        if (!isValid) {

        }

        return isValid;
    }

    public GioSubscription deActivateSubscription(GioSubscription subscription) throws Exception {
        List<GioSubscription> subscriptions = subscriptionRepository.findByOrganizationId(subscription.getOrganizationId());
        if (subscriptions.isEmpty()) {
            throw new Exception("Subscription does not exist");
        }

        GioSubscription gioSubscription = subscriptions.get(0);
        gioSubscription.setActive(1);
        gioSubscription.setUpdated(DateTime.now().toDateTimeISO().toString());
        gioSubscription.setIntUpdated(DateTime.now().getMillis());
        return subscriptionRepository.save(gioSubscription);
    }

    public GioSubscription activateSubscription(GioSubscription subscription) throws Exception {
        List<GioSubscription> subscriptions = subscriptionRepository.findByOrganizationId(subscription.getOrganizationId());
        if (subscriptions.isEmpty()) {
            throw new Exception("Subscription does not exist");
        }

        GioSubscription gioSubscription = subscriptions.get(0);
        gioSubscription.setActive(0);
        gioSubscription.setUpdated(DateTime.now().toDateTimeISO().toString());
        gioSubscription.setIntUpdated(DateTime.now().getMillis());
        return subscriptionRepository.save(gioSubscription);
    }

    private boolean isFreeSubscriptionValid(GioSubscription subscription) throws Exception {

        return true;
    }

    private boolean isCorporateSubscriptionValid(GioSubscription subscription) throws Exception {

        return true;
    }

    private boolean isMonthlySubscriptionValid(GioSubscription subscription) throws Exception {

        return true;
    }

    private boolean isAnnualSubscriptionValid(GioSubscription subscription) throws Exception {

        return true;
    }
}
