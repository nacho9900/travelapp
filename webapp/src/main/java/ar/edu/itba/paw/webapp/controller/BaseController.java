package ar.edu.itba.paw.webapp.controller;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

public abstract class BaseController
{
    @Context
    protected UriInfo uriInfo;

    protected String getFrontendUrl() {
        if ( uriInfo.getBaseUri().getHost().equals( "localhost" ) ) {
            return "localhost:8080";
        }
        else {
            return uriInfo.getBaseUri().getHost() + "/paw-2019a-4";
        }
    }
}
