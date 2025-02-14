package org.apache.clusterbr.zupportl5.enums;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/HttpStatusCodeEnum_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
public enum HttpStatusCodeEnum {
    InvalidStatus(-1),

    /*100*/
    Continue(100),
    SwitchingProtocol(101),
    Processing(102),
    EarlyHints(103),
    
    /*200*/
    OK(200),
    Created(201),
    Accepted(202),
    NonAuthoritativeInformation(203),
    NoContent(204),
    ResetContent(205),
    PartialContent(206),
    MultiStatus(207),
    AlreadyReported(208),
    IMUsed(226),
    
    /*300*/
    MultipleChoice(300),
    MovedPermanently(301),
    Found(302),
    SeeOther(303),
    NotModified(304),
    UseProxy(305),
    Unused(306),
    TemporaryRedirect(307),
    PermanentRedirect(308),
    
    /*400*/
    BadRequest(400),
    Unauthorized(401),
    PaymentRequired(402),
    Forbidden(403),
    NotFound(404),
    MethodNotAllowed(405),
    NotAcceptable(406),
    ProxyAuthenticationRequired(407),
    RequestTimeout(408),
    Conflict(409),
    Gone(410),
    LengthRequired(411),
    PreconditionFailed(412),
    PayloadTooLarge(413),
    URITooLong(414),
    UnsupportedMediaType(415),
    RangeNotSatisfiable(416),
    ExpectationFailed(417),
    ImaTeapot(418),
    MisdirectedRequest(421),
    TooEarly(425),
    UpgradeRequired(426),
    PreconditionRequired(428),
    TooManyRequests(429),
    RequestHeaderFieldsTooLarge(431),
    UnavailableForLegalReasons(451),
    
    /*500*/
    InternalServerError(500),
    NotImplemented(501),
    BadGateway(502),
    ServiceUnavailable(503),
    GatewayTimeout(504),
    HTTPVersionNotSupported(505),
    VariantAlsoNegotiates(506),
    InsufficientStorage(507),
    LoopDetected(508),
    NotExtended(510),
    NetworkAuthenticationRequired(511);     

    private final int code;

    private HttpStatusCodeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
    
    public int value() {
        return this.code;
    }

    public static HttpStatusCodeEnum fromCode(int code) {
        for (HttpStatusCodeEnum status : HttpStatusCodeEnum.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return getDefault();
    }

    private static HttpStatusCodeEnum getDefault() {
        return HttpStatusCodeEnum.InvalidStatus;
    } 
}
