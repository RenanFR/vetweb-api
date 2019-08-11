package com.vetweb.api.utils;

import java.util.HashMap;
import java.util.Map;

public class EmailTemplateBuilder {
	
	public static Map<String, Object> build(EmailType emailType, Object... variablesValues) {
		Map<String, Object> templateModel = new HashMap<>();
		if (emailType == EmailType.FORGOT) {
			templateModel.put("name", variablesValues[0]);
			templateModel.put("product_name", variablesValues[1]);
			templateModel.put("action_url", variablesValues[2]);
			templateModel.put("operating_system", variablesValues[3]);
			templateModel.put("browser_name", variablesValues[4]);
			templateModel.put("support_url", variablesValues[5]);
			templateModel.put("product_url", variablesValues[6]);
			templateModel.put("company_name", variablesValues[7]);
			templateModel.put("company_address", variablesValues[8]);
		}
		return templateModel;
	}

}
