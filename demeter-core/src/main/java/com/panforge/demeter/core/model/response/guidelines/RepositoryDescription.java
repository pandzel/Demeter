/*
 * Copyright 2019 Piotr Andzel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.panforge.demeter.core.model.response.guidelines;

import org.apache.commons.lang3.Validate;

/**
 * Repository description.
 */
public final class RepositoryDescription {
  /** oai identifier */
  public final OaiIdentifier oaiIdentifier;
  /** rights manifest */
  public final RightsManifest rightsManifest;
  /** eprints */
  public final EPrints eprints;
  /** friends */
  public final Friends friends;
  /** branding */
  public final Branding branding;
  /** gateway */
  public final Gateway gateway;

  /**
   * Creates instance of 'repository description' section.
   * @param oaiIdentifier oai identifier (mandatory)
   */
  public RepositoryDescription(OaiIdentifier oaiIdentifier) {
    Validate.notNull(oaiIdentifier, "Missing oaiIdentifier.");
    this.oaiIdentifier = oaiIdentifier;
    this.rightsManifest = null;
    this.eprints = null;
    this.friends = null;
    this.branding = null;
    this.gateway = null;
  }

  /**
   * Creates instance of 'repository description' section.
   * @param rightsManifest rights manifest (mandatory)
   */
  public RepositoryDescription(RightsManifest rightsManifest) {
    Validate.notNull(rightsManifest, "Missing rightsManifest.");
    this.oaiIdentifier = null;
    this.rightsManifest = rightsManifest;
    this.eprints = null;
    this.friends = null;
    this.branding = null;
    this.gateway = null;
  }

  /**
   * Creates instance of 'repository description' section.
   * @param eprints eprints (mandatory)
   */
  public RepositoryDescription(EPrints eprints) {
    Validate.notNull(eprints, "Missing eprints.");
    this.oaiIdentifier = null;
    this.rightsManifest = null;
    this.eprints = eprints;
    this.friends = null;
    this.branding = null;
    this.gateway = null;
  }

  /**
   * Creates instance of 'repository description' section.
   * @param friends friends (mandatory)
   */
  public RepositoryDescription(Friends friends) {
    Validate.notNull(friends, "Missing friends.");
    this.oaiIdentifier = null;
    this.rightsManifest = null;
    this.eprints = null;
    this.friends = friends;
    this.branding = null;
    this.gateway = null;
  }

  /**
   * Creates instance of 'repository description' section.
   * @param branding branding (mandatory)
   */
  public RepositoryDescription(Branding branding) {
    Validate.notNull(branding, "Missing branding.");
    this.oaiIdentifier = null;
    this.rightsManifest = null;
    this.eprints = null;
    this.friends = null;
    this.branding = branding;
    this.gateway = null;
  }

  /**
   * Creates instance of 'repository description' section.
   * @param gateway gateway (mandatory)
   */
  public RepositoryDescription(Gateway gateway) {
    Validate.notNull(gateway, "Missing gateway.");
    this.oaiIdentifier = null;
    this.rightsManifest = null;
    this.eprints = null;
    this.friends = null;
    this.branding = null;
    this.gateway = gateway;
  }

  @Override
  public String toString() {
    return "RepositoryDescription{" + "oaiIdentifier=" + oaiIdentifier + ", rightsManifest=" + rightsManifest + ", eprints=" + eprints + ", friends=" + friends + ", branding=" + branding + ", gateway=" + gateway + '}';
  }
}
