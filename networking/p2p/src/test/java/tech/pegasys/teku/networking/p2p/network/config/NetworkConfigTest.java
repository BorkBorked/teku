/*
 * Copyright Consensys Software Inc., 2022
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package tech.pegasys.teku.networking.p2p.network.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;

@SuppressWarnings("AddressSelection")
class NetworkConfigTest {

  private Optional<String> advertisedIp = Optional.empty();
  private String listenIp = "0.0.0.0";

  @Test
  void getAdvertisedIp_shouldUseAdvertisedAddressWhenSet() {
    final String expected = "1.2.3.4";
    advertisedIp = Optional.of(expected);
    assertThat(createConfig().getAdvertisedIp()).isEqualTo(expected);
  }

  @Test
  void getAdvertisedIp_shouldResolveAnyLocalAdvertisedAddress() {
    advertisedIp = Optional.of("0.0.0.0");
    assertThat(createConfig().getAdvertisedIp()).isNotEqualTo("0.0.0.0");
  }

  @Test
  void getAdvertisedIp_shouldReturnInterfaceIpWhenNotSet() {
    listenIp = "127.0.0.1";
    assertThat(createConfig().getAdvertisedIp()).isEqualTo(listenIp);
  }

  @Test
  void getAdvertisedIp_shouldResolveLocalhostIpWhenInterfaceIpIsAnyLocal() {
    listenIp = "0.0.0.0";
    assertThat(createConfig().getAdvertisedIp()).isNotEqualTo("0.0.0.0");
  }

  @Test
  void getAdvertisedIp_shouldResolveLocalhostIpWhenInterfaceIpIsAnyLocalIpv6() {
    listenIp = "::0";
    final String result = createConfig().getAdvertisedIp();
    assertThat(result).isNotEqualTo("::0");
    assertThat(result).isNotEqualTo("0.0.0.0");
  }

  private NetworkConfig createConfig() {
    return NetworkConfig.builder().advertisedIp(advertisedIp).networkInterface(listenIp).build();
  }
}
