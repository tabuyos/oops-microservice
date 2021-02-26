package com.tabuyos.microservice.oops.common.util.wrapper;

import com.tabuyos.microservice.oops.common.util.page.PageUtil;

/**
 * <p>Description: </p>
 * <pre>
 *   <b>project: </b><i>tabuyos-microservice</i>
 *   <b>package: </b><i>com.tabuyos.microservice.oops.common.util.wrapper</i>
 *   <b>class: </b><i>PageWrapMapper</i>
 *   comment here.
 * </pre>
 *
 * @author <pre><b>username: </b><i><a href="http://www.tabuyos.com">Tabuyos</a></i></pre>
 * <pre><b>site: </b><i><a href="http://www.tabuyos.com">http://www.tabuyos.com</a></i></pre>
 * <pre><b>email: </b><i>tabuyos@outlook.com</i></pre>
 * <pre><b>description: </b><i>
 *   <pre>
 *     Talk is cheap, show me the code.
 *   </pre>
 * </i></pre>
 * @version 0.1.0
 * @since 0.1.0 - 2/22/21 10:09 AM
 */
public class PageWrapMapper {

	/**
	 * Instantiates a new page wrap mapper.
	 */
	private PageWrapMapper() {
	}

	private static <E> PageWrapper<E> wrap(int code, String message, E o, PageUtil pageUtil) {
    return new PageWrapper<>(code, message, o, pageUtil);
	}

	/**
	 * Wrap data with default code=200.
	 *
	 * @param <E>      the type parameter
	 * @param o        the o
	 * @param pageUtil the page util
	 *
	 * @return the page wrapper
	 */
	public static <E> PageWrapper<E> wrap(E o, PageUtil pageUtil) {
		return wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, o, pageUtil);
	}

	/**
	 * Wrap.
	 *
	 * @param <E>     the type parameter
	 * @param code    the code
	 * @param message the message
	 *
	 * @return the page wrapper
	 */
	public static <E> PageWrapper<E> wrap(int code, String message) {
		return wrap(code, message, null, null);
	}

	/**
	 * Wrap.
	 *
	 * @param <E>  the type parameter
	 * @param code the code
	 *
	 * @return the page wrapper
	 */
	public static <E> PageWrapper<E> wrap(int code) {
		return wrap(code, null, null, null);
	}

	/**
	 * Wrap.
	 *
	 * @param <E> the type parameter
	 * @param e   the e
	 *
	 * @return the page wrapper
	 */
	public static <E> PageWrapper<E> wrap(Exception e) {
		return new PageWrapper<E>(Wrapper.ERROR_CODE, e.getMessage(), null, null);
	}

	/**
	 * Un wrapper.
	 *
	 * @param <E>     the type parameter
	 * @param wrapper the wrapper
	 *
	 * @return the e
	 */
	public static <E> E unWrap(PageWrapper<E> wrapper) {
		return wrapper.getResult();
	}

	/**
	 * Wrap ERROR. code=100
	 *
	 * @param <E> the type parameter
	 *
	 * @return the page wrapper
	 */
	public static <E> PageWrapper<E> illegalArgument() {
		return wrap(Wrapper.ILLEGAL_ARGUMENT_CODE_, Wrapper.ILLEGAL_ARGUMENT_MESSAGE, null, null);
	}

	/**
	 * Wrap ERROR. code=500
	 *
	 * @param <E> the type parameter
	 *
	 * @return the page wrapper
	 */
	public static <E> PageWrapper<E> error() {
		return wrap(Wrapper.ERROR_CODE, Wrapper.ERROR_MESSAGE, null, null);
	}

	/**
	 * Wrap SUCCESS. code=200
	 *
	 * @param <E> the type parameter
	 *
	 * @return the page wrapper
	 */
	public static <E> PageWrapper<E> ok() {
		return new PageWrapper<E>();
	}
}
