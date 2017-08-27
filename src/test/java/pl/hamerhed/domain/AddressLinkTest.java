package pl.hamerhed.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AddressLinkTest {

	@Test
	public void testAddressLink(){
		AddressLink item = new AddressLink("www.link.pl/:param1:/x/y/", null, new String[]{":param1:"});
		
		assertEquals("www.link.pl/1/x/y/", item.updateLinkParams(new String[]{"1"}));
		assertEquals("www.link.pl/myParameter/x/y/", item.updateLinkParams(new String[]{"myParameter"}));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddressLink2Params1Value(){
		AddressLink item = new AddressLink("www.link.pl/:param1:/x/y/", null, new String[]{":param1:", ":test:"});
		assertEquals("www.link.pl/1/x/y/", item.updateLinkParams(new String[]{"1"}));
		fail("should be Exception");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddressNullParams(){
		AddressLink item = new AddressLink("www.link.pl/:param1:/x/y/", null, new String[]{":param1:", ":test:"});
		assertEquals("www.link.pl/1/x/y/", item.updateLinkParams(null));
		fail("should be Exception");
	}
	
	@Test
	public void testAddressLink2Params(){
		AddressLink item = new AddressLink("www.link.pl/:param1:/x/y/:test:", null, new String[]{":param1:", ":test:"});
		assertEquals("www.link.pl/1/x/y/second-parameter", item.updateLinkParams(new String[]{"1", "second-parameter"}));
	}
	
	@Test
	public void testNonParamAddressLink(){
		AddressLink item = new AddressLink("www.link.pl/x/y/", null, new String[]{});
		assertEquals("www.link.pl/x/y/", item.updateLinkParams(new String[]{}));
	}
}
