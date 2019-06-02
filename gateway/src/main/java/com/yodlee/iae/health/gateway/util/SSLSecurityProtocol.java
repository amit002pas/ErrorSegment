/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.gateway.util;
public enum SSLSecurityProtocol {
	TLSv1_2 {
		public String toString() {
			return "TLSv1.2";
		}
	},
	TLSv1_1 {
		public String toString() {
			return "TLSv1.1";
		}
	},
	TLSv1 {
		public String toString() {
			return "TLSv1";
		}
	},
	SSLv3 {
		public String toString() {
			return "SSLv3";
		}
	}
}