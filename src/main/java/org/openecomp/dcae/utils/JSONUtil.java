package org.openecomp.dcae.utils;



import java.io.IOException;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public final class JSONUtil {
    
    public static final String getJSONWithoutNull(Object obj) throws IOException {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
            return mapper.writeValueAsString(obj);
        } catch (IOException ex) {
        	 throw  ex;
        }
    }

    public static final String getJSONString(Object obj) throws IOException {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
            return mapper.writeValueAsString(obj);
        } catch (IOException ex) {
            throw  ex;
        }
    }

    public static <T> T convertJSONtoObject(final String jsonStr, Class<T> objClass) throws IOException {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonStr, objClass);
        } catch (IOException ex) {
            throw ex;
        }
    }
    
    public static <T> List<T> convertJSONtoObjectList(final String jsonStr, Class<T> objClass) throws IOException {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonStr, mapper.getTypeFactory().constructCollectionType(List.class, objClass));
        } catch (IOException ex) {
            throw ex;
        }
    }

    public static void main(String args[]) {
        try {
//            String jsonStr =
//                "{\"solution\":{\"alias\":\"Solution3\",\"opportunityId\":\"1-10P7\",\"account\":{\"accountRowId\":\"1\",\"ccId\":\"1\",\"marketStrata\":\"BNS\",\"customerType\":\"Retail\",\"customerLegalInfo\":{\"customerLegalName\":\"CocaCola\",\"streetAddress1\":\"King Street\",\"streetAddress2\":\"Oakwood drive\",\"city\":\"MiddleTown\",\"stateOrProvince\":\"NJ\",\"country\":\"US\",\"zipOrPostalCode\":\"1784\",\"telephone\":\"8608977845\"}}},\"pricingScenario\":{\"offerInstances\":[{\"offerId\":\"100\",\"offerName\":\"Microsoft Office 365\",\"pricePlanInstances\":[{\"pricePlanId\":\"100\",\"pricePlanName\":\"MSO365\",\"productInstances\":[{\"productID\":\"100\",\"productName\":\"MSO365\"}],\"typeOfContractActivity\":\"New\",\"contract\":{\"contacts\":[{\"contactType\":\"Customer\",\"firstName\":\"John\",\"lastName\":\"Simpson\",\"title\":\"MR.\",\"streetAddress\":\"200S Laurel ave.\",\"city\":\"Middletown\",\"state\":\"NJ\",\"country\":\"US\",\"zip\":\"07748\",\"telephone\":\"999-999-9999\",\"fax\":\"\",\"email\":\"js1753@att.com\",\"scvpName\":\"Jim\",\"branchManager\":\"Chip Wood\",\"salesStrata\":\"BNS\",\"salesRegion\":\"NorthEast\",\"agentCode\":\"123\",\"companyCode\":\"ATT\"},{\"contactType\":\"ATT Sales\",\"firstName\":\"Rohit\",\"lastName\":\"Patel\",\"title\":\"MR.\",\"streetAddress\":\"200S Laurel ave.\",\"city\":\"Middletown\",\"state\":\"NJ\",\"country\":\"US\",\"zip\":\"07748\",\"telephone\":\"999-999-9999\",\"fax\":\"\",\"email\":\"js1753@att.com\",\"scvpName\":\"Jim\",\"branchManager\":\"Chip Wood\",\"salesStrata\":\"BNS\",\"salesRegion\":\"NorthEast\",\"agentCode\":\"123\",\"companyCode\":\"ATT\"},{\"contactType\":\"ATT Solution Provider\",\"firstName\":\"Vivek\",\"lastName\":\"Nayak\",\"title\":\"MR.\",\"streetAddress\":\"200S Laurel ave.\",\"city\":\"Middletown\",\"state\":\"NJ\",\"country\":\"US\",\"zip\":\"07748\",\"telephone\":\"999-999-9999\",\"fax\":\"\",\"email\":\"js1753@att.com\",\"scvpName\":\"Jim\",\"branchManager\":\"Chip Wood\",\"salesStrata\":\"BNS\",\"salesRegion\":\"NorthEast\",\"agentCode\":\"123\",\"companyCode\":\"ATT\"}],\"masterAgreement\":{\"maType\":\"\",\"maNumber\":\"\"},\"masterAgreementRequest\":{\"maType\":\"New\"}},\"commitmentInstances\":[{\"minAnnualRevCommitment\":\"10000\",\"contractTerm\":\"12\"}],\"pricePlanProductInstances\":[{\"isoCountryCode\":\"US\",\"ratePlanId\":\"MSO1\",\"discountInstances\":[{\"discountId\":\"3000\",\"discountPercentage\":\"10\"},{\"discountId\":\"3001\",\"discountPercentage\":\"20\"}],\"contactInfo\":[{\"contactType\":\"AdminForCountry\",\"firstName\":\"John\",\"lastName\":\"Simpson\",\"email\":\"js1753@att.com\",\"adminId\":\"js1753\"}],\"genericFeatures\":[{\"typeOfFeature\":\"PlanLicense\",\"components\":[{\"id\":\"nnn\",\"quantity\":\"7\"},{\"id\":\"mmm\",\"quantity\":\"16\"}]}]}]}]},{\"offerId\":\"100\",\"offerName\":\"Standalone Business Voice Over IP\",\"pricePlanInstances\":[{\"pricePlanId\":\"3\",\"pricePlanName\":\"BVoIP Standalone\",\"productInstances\":[{\"productID\":\"3\",\"productName\":\"BVOIP\"}],\"typeOfContractActivity\":\"New\",\"contract\":{\"contacts\":[{\"contactType\":\"Customer\",\"firstName\":\"John\",\"lastName\":\"Simpson\",\"title\":\"MR.\",\"streetAddress\":\"200S Laurel ave.\",\"city\":\"Middletown\",\"state\":\"NJ\",\"country\":\"US\",\"zip\":\"07748\",\"telephone\":\"999-999-9999\",\"fax\":\"\",\"email\":\"js1753@att.com\",\"scvpName\":\"Jim\",\"branchManager\":\"Chip Wood\",\"salesStrata\":\"BNS\",\"salesRegion\":\"NorthEast\",\"agentCode\":\"123\",\"companyCode\":\"ATT\"},{\"contactType\":\"ATT Sales\",\"firstName\":\"Rohit\",\"lastName\":\"Patel\",\"title\":\"MR.\",\"streetAddress\":\"200S Laurel ave.\",\"city\":\"Middletown\",\"state\":\"NJ\",\"country\":\"US\",\"zip\":\"07748\",\"telephone\":\"999-999-9999\",\"fax\":\"\",\"email\":\"js1753@att.com\",\"scvpName\":\"Jim\",\"branchManager\":\"Chip Wood\",\"salesStrata\":\"BNS\",\"salesRegion\":\"NorthEast\",\"agentCode\":\"123\",\"companyCode\":\"ATT\"},{\"contactType\":\"ATT Solution Provider\",\"firstName\":\"Vivek\",\"lastName\":\"Nayak\",\"title\":\"MR.\",\"streetAddress\":\"200S Laurel ave.\",\"city\":\"Middletown\",\"state\":\"NJ\",\"country\":\"US\",\"zip\":\"07748\",\"telephone\":\"999-999-9999\",\"fax\":\"\",\"email\":\"js1753@att.com\",\"scvpName\":\"Jim\",\"branchManager\":\"Chip Wood\",\"salesStrata\":\"BNS\",\"salesRegion\":\"NorthEast\",\"agentCode\":\"123\",\"companyCode\":\"ATT\"}],\"masterAgreement\":{\"maType\":\"\",\"maNumber\":\"\"},\"masterAgreementRequest\":{\"maType\":\"New\"}},\"commitmentInstances\":[{\"minAnnualRevCommitment\":\"10000\",\"contractTerm\":\"12\"}],\"pricePlanProductInstances\":[{\"isoCountryCode\":\"US\",\"ratePlanId\":\"MSO1\",\"discountInstances\":[{\"discountId\":\"239\",\"discountPercentage\":\"10\"},{\"discountId\":\"240\",\"discountPercentage\":\"20\"},{\"discountId\":\"242\",\"discountPercentage\":\"10\"},{\"discountId\":\"245\",\"discountPercentage\":\"20\"},{\"discountId\":\"246\",\"discountPercentage\":\"20\"}],\"contactInfo\":[{\"contactType\":\"AdminForCountry\",\"firstName\":\"John\",\"lastName\":\"Simpson\",\"email\":\"js1753@att.com\",\"adminId\":\"js1753\"}],\"genericFeatures\":[{\"typeOfFeature\":\"PlanLicense\",\"components\":[{\"id\":\"nnn\",\"quantity\":\"7\"},{\"id\":\"mmm\",\"quantity\":\"16\"}]}]}]}]}]}}";
           // ContractGenerationRequest contractGenerationRequest =
             //   convertJSONtoObject(jsonStr, ContractGenerationRequest.class);
         //   System.out.println(contractGenerationRequest.getSolution().getOpportunityId());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
