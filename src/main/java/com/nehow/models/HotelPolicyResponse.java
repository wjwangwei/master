package com.nehow.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Igbalajobi Jamiu Okunade on 4/25/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelPolicyResponse {
    private Policies[] cancellations;
    private Policies supplierPolicies;
}
