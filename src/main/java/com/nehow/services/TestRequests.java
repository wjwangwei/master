package com.nehow.services;

/**
 * Created by Igbalajobi Jamiu Okunade on 4/22/17.
 */
public class TestRequests {
    public static final String searchRequest = "{\n" +
            "  \"request\": {\n" +
            "    \"countryId\": \"TH\",\n" +
            "    \"cityId\": \"48201\",\n" +
            "    \"nationality\": \"CN\",\n" +
            "\t\"checkIn\": \"2017-05-22\",\n" +
            "    \"checkOut\": \"2017-05-25\",\n" +
            "    \"rooms\": [\n" +
            "      {\n" +
            "        \"roomIndex\": \"1\",\n" +
            "        \"rooms\": 1,\n" +
            "        \"adults\": 2,\n" +
            "        \"children\": 0,\n" +
            "        \"roomType\": \"\",\n" +
            "        \"roomRateCode\": null,\n" +
            "        \"ages\": null\n" +
            "      }\n" +
            "    ],\n" +
            "    \"currency\": \"USD\",\n" +
            "    \"queryId\": \"e83dd6a9b5330f881d4e662d78334561\",\n" +
            "    \"supplierId\": \"expedia,gta,veturis,restel,ivector\"\n" +
            "  },\n" +
            " \"exchangeRates\": [\n" +
            "    {\n" +
            "      \"id\": 0,\n" +
            "      \"date\": null,\n" +
            "      \"currency0\": \"HKD\",\n" +
            "      \"currency1\": \"CNY\",\n" +
            "      \"rate\": 0.8869,\n" +
            "      \"rateTime\": null\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 0,\n" +
            "      \"date\": null,\n" +
            "      \"currency0\": \"EUR\",\n" +
            "      \"currency1\": \"CNY\",\n" +
            "      \"rate\": 7.3278,\n" +
            "      \"rateTime\": null\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 0,\n" +
            "      \"date\": null,\n" +
            "      \"currency0\": \"GBP\",\n" +
            "      \"currency1\": \"CNY\",\n" +
            "      \"rate\": 8.4528,\n" +
            "      \"rateTime\": null\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 0,\n" +
            "      \"date\": null,\n" +
            "      \"currency0\": \"USD\",\n" +
            "      \"currency1\": \"CNY\",\n" +
            "      \"rate\": 6.882,\n" +
            "      \"rateTime\": null\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 0,\n" +
            "      \"date\": null,\n" +
            "      \"currency0\": \"CNY\",\n" +
            "      \"currency1\": \"CNY\",\n" +
            "      \"rate\": 1,\n" +
            "      \"rateTime\": null\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 0,\n" +
            "      \"date\": null,\n" +
            "      \"currency0\": \"CNY\",\n" +
            "      \"currency1\": \"CNY\",\n" +
            "      \"rate\": 1,\n" +
            "      \"rateTime\": null\n" +
            "    }\n" +
            "  ],\n" +
            "  \"markups\": {\n" +
            "    \"dotw\": 3,\n" +
            "    \"ivector\": 1.5,\n" +
            "    \"sujaba\": 2,\n" +
            "    \"miki\": 2,\n" +
            "    \"ptclub\": 2,\n" +
            "    \"hotelspro\": 2,\n" +
            "    \"jac\": 1.5,\n" +
            "    \"jltour\": 2,\n" +
            "    \"travellanda\": 2,\n" +
            "    \"qunar\": 2,\n" +
            "    \"gta\": 1,\n" +
            "    \"restel\": 2,\n" +
            "    \"travco\": 2,\n" +
            "    \"alaba\": 2,\n" +
            "    \"forever\": 2,\n" +
            "    \"hotelbeds\": 2,\n" +
            "    \"veturis\": 5,\n" +
            "    \"tourico\": 2\n" +
            "  },\n" +
            "  \"starRating\": {\n" +
            "    \"low\": 0,\n" +
            "    \"high\": 100\n" +
            "  },\n" +
            "  \"score\": 0,\n" +
            "  \"sort\": {\n" +
            "    \"field\": \"rate\",\n" +
            "    \"mode\": \"ascend\"\n" +
            "  },\n" +
            "  \"limit\": {\n" +
            "    \"start\": 0,\n" +
            "    \"length\": 20\n" +
            "  }\n" +
            "}\n";

    public static final String availabilityRequest = "{\n" +
            "  \"request\": {\n" +
            "    \"countryId\": \"TH\",\n" +
            "    \"cityId\": \"48201\",\n" +
            "    \"nationality\": \"CN\",\n" +
            "\t\"checkIn\": \"2017-05-22\",\n" +
            "    \"checkOut\": \"2017-05-25\",\n" +
            "    \"rooms\": [\n" +
            "      {\n" +
            "        \"roomIndex\": \"1\",\n" +
            "        \"rooms\": 1,\n" +
            "        \"adults\": 2,\n" +
            "        \"children\": 0,\n" +
            "        \"roomType\": \"\",\n" +
            "        \"roomRateCode\": null,\n" +
            "        \"ages\": null\n" +
            "      }\n" +
            "    ],\n" +
            "    \"currency\": \"USD\",\n" +
            "    \"queryId\": \"e83dd6a9b5330f881d4e662d78334561\",\n" +
            "    \"supplierId\": \"expedia,gta,veturis,restel,ivector\"\n" +
            "  },\n" +
            " \"exchangeRates\": [\n" +
            "    {\n" +
            "      \"id\": 0,\n" +
            "      \"date\": null,\n" +
            "      \"currency0\": \"HKD\",\n" +
            "      \"currency1\": \"CNY\",\n" +
            "      \"rate\": 0.8869,\n" +
            "      \"rateTime\": null\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 0,\n" +
            "      \"date\": null,\n" +
            "      \"currency0\": \"EUR\",\n" +
            "      \"currency1\": \"CNY\",\n" +
            "      \"rate\": 7.3278,\n" +
            "      \"rateTime\": null\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 0,\n" +
            "      \"date\": null,\n" +
            "      \"currency0\": \"GBP\",\n" +
            "      \"currency1\": \"CNY\",\n" +
            "      \"rate\": 8.4528,\n" +
            "      \"rateTime\": null\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 0,\n" +
            "      \"date\": null,\n" +
            "      \"currency0\": \"USD\",\n" +
            "      \"currency1\": \"CNY\",\n" +
            "      \"rate\": 6.882,\n" +
            "      \"rateTime\": null\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 0,\n" +
            "      \"date\": null,\n" +
            "      \"currency0\": \"CNY\",\n" +
            "      \"currency1\": \"CNY\",\n" +
            "      \"rate\": 1,\n" +
            "      \"rateTime\": null\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 0,\n" +
            "      \"date\": null,\n" +
            "      \"currency0\": \"CNY\",\n" +
            "      \"currency1\": \"CNY\",\n" +
            "      \"rate\": 1,\n" +
            "      \"rateTime\": null\n" +
            "    }\n" +
            "  ],\n" +
            "  \"markups\": {\n" +
            "    \"dotw\": 3,\n" +
            "    \"ivector\": 1.5,\n" +
            "    \"sujaba\": 2,\n" +
            "    \"miki\": 2,\n" +
            "    \"ptclub\": 2,\n" +
            "    \"hotelspro\": 2,\n" +
            "    \"jac\": 1.5,\n" +
            "    \"jltour\": 2,\n" +
            "    \"travellanda\": 2,\n" +
            "    \"qunar\": 2,\n" +
            "    \"gta\": 1,\n" +
            "    \"restel\": 2,\n" +
            "    \"travco\": 2,\n" +
            "    \"alaba\": 2,\n" +
            "    \"forever\": 2,\n" +
            "    \"hotelbeds\": 2,\n" +
            "    \"veturis\": 5,\n" +
            "    \"tourico\": 2\n" +
            "  },\n" +
            "  \"starRating\": {\n" +
            "    \"low\": 0,\n" +
            "    \"high\": 100\n" +
            "  },\n" +
            "  \"score\": 0,\n" +
            "  \"sort\": {\n" +
            "    \"field\": \"rate\",\n" +
            "    \"mode\": \"ascend\"\n" +
            "  },\n" +
            "  \"limit\": {\n" +
            "    \"start\": 0,\n" +
            "    \"length\": 20\n" +
            "  }\n" +
            "}\n";

    public static final String exchangeRates = "[\n" +
            "    {\n" +
            "      \"id\": 0,\n" +
            "      \"date\": null,\n" +
            "      \"currency0\": \"HKD\",\n" +
            "      \"currency1\": \"CNY\",\n" +
            "      \"rate\": 0.8869,\n" +
            "      \"rateTime\": null\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 0,\n" +
            "      \"date\": null,\n" +
            "      \"currency0\": \"EUR\",\n" +
            "      \"currency1\": \"CNY\",\n" +
            "      \"rate\": 7.3278,\n" +
            "      \"rateTime\": null\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 0,\n" +
            "      \"date\": null,\n" +
            "      \"currency0\": \"GBP\",\n" +
            "      \"currency1\": \"CNY\",\n" +
            "      \"rate\": 8.4528,\n" +
            "      \"rateTime\": null\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 0,\n" +
            "      \"date\": null,\n" +
            "      \"currency0\": \"USD\",\n" +
            "      \"currency1\": \"CNY\",\n" +
            "      \"rate\": 6.882,\n" +
            "      \"rateTime\": null\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 0,\n" +
            "      \"date\": null,\n" +
            "      \"currency0\": \"CNY\",\n" +
            "      \"currency1\": \"CNY\",\n" +
            "      \"rate\": 1,\n" +
            "      \"rateTime\": null\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 0,\n" +
            "      \"date\": null,\n" +
            "      \"currency0\": \"CNY\",\n" +
            "      \"currency1\": \"CNY\",\n" +
            "      \"rate\": 1,\n" +
            "      \"rateTime\": null\n" +
            "    }\n" +
            "  ]";

    public static final String policyRequest = "{\n" +
            "    \"queryId\": \"183493fc7c8e683877bfc45b8b6d2859\",\n" +
            "    \"supplierId\": \"ivector\",\n" +
            "    \"hotelId\": \"837576\",\n" +
            "    \"hotelCode\": \"136097\",\n" +
            "    \"checkIn\": \"2017-05-09\",\n" +
            "    \"checkOut\": \"2017-05-11\",\n" +
            "    \"policyCodes\": [\n" +
            "        \"[\\\"1370193\\\",\\\"0\\\",\\\"S+42cRV+i/UVc8bv/4m/ZJVz+29bFRgm0EbB46pDZy01RhgkOkflyymvkkeUaMxCkRKAjAa4wkd0FBGKZpVzgt6sRoptW12lPSOZ6D8bBCA=\\\",\\\"1\\\",\\\"3\\\",\\\"1,,1,1,0:8\\\"]\"\n" +
            "    ],\n" +
            "    \"exchangeRates\": [\n" +
            "        {\n" +
            "            \"id\": 0,\n" +
            "            \"date\": null,\n" +
            "            \"currency0\": \"HKD\",\n" +
            "            \"currency1\": \"CNY\",\n" +
            "            \"rate\": 0.8869,\n" +
            "            \"rateTime\": null\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 0,\n" +
            "            \"date\": null,\n" +
            "            \"currency0\": \"EUR\",\n" +
            "            \"currency1\": \"CNY\",\n" +
            "            \"rate\": 7.3278,\n" +
            "            \"rateTime\": null\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 0,\n" +
            "            \"date\": null,\n" +
            "            \"currency0\": \"GBP\",\n" +
            "            \"currency1\": \"CNY\",\n" +
            "            \"rate\": 8.4528,\n" +
            "            \"rateTime\": null\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 0,\n" +
            "            \"date\": null,\n" +
            "            \"currency0\": \"USD\",\n" +
            "            \"currency1\": \"CNY\",\n" +
            "            \"rate\": 6.882,\n" +
            "            \"rateTime\": null\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 0,\n" +
            "            \"date\": null,\n" +
            "            \"currency0\": \"CNY\",\n" +
            "            \"currency1\": \"CNY\",\n" +
            "            \"rate\": 1,\n" +
            "            \"rateTime\": null\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 0,\n" +
            "            \"date\": null,\n" +
            "            \"currency0\": \"CNY\",\n" +
            "            \"currency1\": \"CNY\",\n" +
            "            \"rate\": 1,\n" +
            "            \"rateTime\": null\n" +
            "        }\n" +
            "    ],\n" +
            "    \"markups\": {\n" +
            "        \"ivector\": 1\n" +
            "    },\n" +
            "    \"safeDay\": 3\n" +
            "}\n";
    public static final String request = "{\"cityId\": 48201,\n" +
            "    \"countryId\": \"45\",\n" +
            "    \"checkIn\": \"2017-04-27\",\n" +
            "    \"checkOut\": \"2017-04-29\",\n" +
            "    \"rooms\": [\n" +
            "      {\n" +
            "        \"roomIndex\": \"1\",\n" +
            "        \"rooms\": 1,\n" +
            "        \"adults\": 2,\n" +
            "        \"children\": 0,\n" +
            "        \"roomType\": \"\",\n" +
            "        \"roomRateCode\": null,\n" +
            "        \"ages\": null\n" +
            "      }\n" +
            "    ],\n" +
            "    \"nationality\": \"CN\",\n" +
            "    \"currency\": \"CNY\",\n" +
            "    \"queryId\": \"c75f8deaf3494ea225c3e0c4a27456d0\",\n" +
            "    \"supplierId\": \"expedia,gta,veturis,restel,ivector\"}";
}
