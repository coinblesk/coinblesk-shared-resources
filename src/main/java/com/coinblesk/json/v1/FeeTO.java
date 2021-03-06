/*
 * Copyright 2016 The Coinblesk team and the CSG Group at University of Zurich
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.coinblesk.json.v1;

/**
 *
 * @author Thomas Bocek
 */
public class FeeTO extends BaseTO<FeeTO> {

    private Integer fee;
    
    public Integer fee() {
        return fee;
    }

    public FeeTO fee(Integer fee) {
        this.fee = fee;
        return this;
    }
}
