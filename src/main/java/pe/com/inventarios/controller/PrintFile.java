package pe.com.inventarios.controller;

public class PrintFile {
	
	static String path = "C:\\Users\\sandovlu\\Dropbox\\Comenzar.pdf";

//	public static void main(String[] args) {
//
//		FileInputStream psStream = null;
//		try {
//			psStream = new FileInputStream(path);
//		} catch (FileNotFoundException ffne) {
//			ffne.printStackTrace();
//		}
//		if (psStream == null) {
//			return;
//		}
//
//		DocFlavor psInFormat = DocFlavor.INPUT_STREAM.PDF;
//		Doc myDoc = new SimpleDoc(psStream, psInFormat, null);
//
//		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
//		aset.add(MediaSizeName.ISO_A4);
//		aset.add(OrientationRequested.LANDSCAPE);
//		PrintService[] services = PrintServiceLookup.lookupPrintServices(psInFormat, aset);
////		PrintService myPrinter = PrintServiceLookup.lookupDefaultPrintService();
//		
//
//		PrintService myPrinter = null;
//		for (int i = 0; i < services.length; i++) {
//			String svcName = services[i].toString();
//			System.out.println("service found: " + svcName);
//			if (svcName.contains("printer closest to me")) {
//				myPrinter = services[i];
//				System.out.println("my printer found: " + svcName);
//				break;
//			}
//		}
//
//		if (myPrinter != null) {
//			AttributeSet attributes = myPrinter.getAttributes();
//			for (Attribute a : attributes.toArray()) {
//				String name = a.getName();
//				String value = attributes.get(a.getClass()).toString();
//				System.out.println(name + " : " + value);
//			}
//
//			DocPrintJob job = myPrinter.createPrintJob();
//			try {
//				job.print(myDoc, aset);
//			} catch (Exception pe) {
//				pe.printStackTrace();
//			}
//		} else {
//			System.out.println("no printer services found");
//		}
//	}
	  
}
