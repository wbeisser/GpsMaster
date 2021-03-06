package org.gpsmaster.gpsloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import javolution.io.Struct;

import org.gpsmaster.Const;
import org.gpsmaster.gpxpanel.GPXExtension;
import org.gpsmaster.gpxpanel.GPXFile;
import org.gpsmaster.gpxpanel.Track;
import org.gpsmaster.gpxpanel.Waypoint;
import org.gpsmaster.gpxpanel.WaypointGroup;
import org.joda.time.DateTime;

/**
 * Loader for .CPO files generated by DSW devices
 * http://www.energympro.com/
 *
 * @author rfu
 *
 * NOTE: information contained in POINT structure
 */
public class CpoLoader extends GpsLoader {

	private final double LATLONSCALE = 1000000;
	private final int WORKOUTSIZE = 80; // length of Workout structure
	private final int LAPSIZE = 44; // length of Lap structure
	private long pointRecs = 0;
	private DateTime gpsDate = null;
	private int lapNum = 0;  // number of laps in file

	private List<Lap> laps = new ArrayList<CpoLoader.Lap>();

	/**
	 * Class representing the java equivalent of "typedef struct LAP"
	 *
	 * @author rfu
	 *
	 */
	private static class Lap extends Struct {
		public final Unsigned32 splitTime = new Unsigned32(); // ????
		public final Unsigned32 totalTime = new Unsigned32(); // ignored
		public final Unsigned16 number = new Unsigned16();
		public final Unsigned32 distance = new Unsigned32(); // ignored
		public final Unsigned16 calory = new Unsigned16();
		public final UTF8String res1 = new UTF8String(20); // reserved or ignored
		public final Unsigned16 startPt = new Unsigned16();
		public final Unsigned16 endPt = new Unsigned16();

		public ByteOrder byteOrder() {
	         // CPO is obviously using:
	         return ByteOrder.LITTLE_ENDIAN;
	    }

	}

	/**
	 * Class representing the java equivalent of "typedef struct WORKOUT"
	 *
	 * @author rfu
	 *
	 */
	private static class Workout extends Struct {
		public final Unsigned8 dateYear = new Unsigned8();
		public final Unsigned8 dateMonth = new Unsigned8();
		public final Unsigned8 dateDay = new Unsigned8();
		public final Unsigned8 timeHour = new Unsigned8();
		public final Unsigned8 timeMinute = new Unsigned8();
		public final Unsigned8 timeSecond = new Unsigned8();
		public final Unsigned16 totalRecPt = new Unsigned16();
		public final Unsigned32 totalTime = new Unsigned32(); // total time 1/10th seconds
		public final Unsigned32 totalDist = new Unsigned32(); // in meters
		public final Unsigned16 lapNum = new Unsigned16();
		public final Unsigned16 calory = new Unsigned16();
		public final UTF8String res2 = new UTF8String(24); // reserved or ignored
		public final UTF8String product = new UTF8String(15);
		public final Unsigned16 verNum = new Unsigned16();

		public ByteOrder byteOrder() {
			// CPO is obviously using:
	         return ByteOrder.LITTLE_ENDIAN;
	    }

		// we ignore the rest, which shouldn't matter since
		// this record is at the end of the file

	}

	/**
	 * Class representing the java equivalent of "typedef struct POINT"
	 *
	 * @author rfu
	 *
	 */
	private static class Point extends Struct {
		public final Signed32 lat = new Signed32();
		public final Signed32 lon = new Signed32();
		public final Unsigned16 alt = new Unsigned16();
		public final Signed16 res1 = new Signed16(); // reserved or ignored
		public final Signed32 gpsSpeed = new Signed32();
		public final Unsigned16 intDist = new Unsigned16(); // interval distance (cm)
		public final Unsigned16 res2 = new Unsigned16();
		public final Signed32 intTime = new Signed32(); // interval time
		public final Unsigned8 gpsStatus = new Unsigned8();
		public final Unsigned8 hrHr = new Unsigned8();
		public final Unsigned8 hrStatus = new Unsigned8();
		public final Unsigned8 res3 = new Unsigned8();
		public final Unsigned32 speedSpeed = new Unsigned32();
		public final Unsigned8 speedStatus = new Unsigned8();
		public final UTF8String speedRes = new UTF8String(3); // reserved
		public final Unsigned8 cadCadence = new Unsigned8();
		public final Unsigned8 cadStatus = new Unsigned8();
		public final Unsigned16 pwrCadence = new Unsigned16();
		public final Unsigned16 pwrPower = new Unsigned16();
		public final Unsigned8 pwrStatus = new Unsigned8();
		public final Unsigned8 res4 = new Unsigned8();
		public final Signed8 temp = new Signed8();
		public final UTF8String dummy = new UTF8String(3);

		public ByteOrder byteOrder() {
	         // // CPO is obviously using:
	         return ByteOrder.LITTLE_ENDIAN;
	    }


	}

	/**
	 * Constructor
	 */
	public CpoLoader() {
		super();
		extensions.add("cpo");
	}

	/**
	 * This method assumes that the InputStream is based on a buffer
	 * which allows random access to the whole content.
#	 */

	@Override
	public GPXFile load(InputStream inStream, String format) throws Exception {

		if (inStream.markSupported() == false) {
			throw new IOException("unspported stream type");
		}

		// DANGEROUS the following assumes that inStream.available()
		// returns the TOTAL length of the FILE and not just what
		// actually fits in some buffer.
		// In the new context of the filehub, this seems to be the case
		// with the implemented ByteArrayInputStream buffer
		int len = inStream.available();

		gpx = new GPXFile();
		gpx.addExtensionPrefix(Const.EXT_HRM);

		inStream.mark(len);

		// read header at the end of file
		skipBytes(inStream, len - WORKOUTSIZE);
		readWorkout(inStream);

		// read lap definition
		inStream.reset();
		skipBytes(inStream, len - WORKOUTSIZE - LAPSIZE * lapNum);
		readLaps(inStream);

		// read GPS points
		inStream.reset();
		readPoints(inStream);
		inStream.close();

		return gpx;

	}

	/**
	 * read Workout structure at the end of the file.
	 * file position needs to be set to the start
	 * of the record.
	 *
	 * @param inStream
	 * @throws IOException
	 */
	private void readWorkout(InputStream inStream) throws IOException {

		Workout w = new Workout();
		w.read(inStream);

		pointRecs = w.totalRecPt.get();
		gpx.setCreator(String.format("%s v%d", w.product.get(), w.verNum.get()));

		gpsDate = new DateTime(w.dateYear.get() + 2000,
				w.dateMonth.get(),
				w.dateDay.get(),
				w.timeHour.get(),
				w.timeMinute.get(),
				w.timeSecond.get());
		gpx.getMetadata().setTime(gpsDate.toDate()); // timezone?
		int cal = w.calory.get();
		if (cal > 0) {
			gpx.getExtension().add(new GPXExtension(Const.EXT_HRMCAL, Integer.toString(cal)));
		}
		lapNum =  w.lapNum.get(); // number of laps

	}

	/**
	 *
	 * @param inStream
	 * @throws IOException
	 */
	private void readLaps(InputStream inStream) throws IOException {
		for (int i = 0; i < lapNum; i++) {
			Lap lap = new Lap();
			lap.read(inStream);
			laps.add(lap);
		}
	}

	/**
	 *
	 * This code assumes that the first (and maybe only) lap record
	 * contains (0 - 0) (startPt = 0, endPt = 0)
	 *
	 * @param inStream
	 * @throws IOException
	 */
	private void readPoints(InputStream inStream) throws IOException {

		int lapEndPt = 0;
		int lapIdx = 0;
		int totalTime = 0;

		WaypointGroup trackSeg = null;
		Track track = new Track(gpx.getColor());
		gpx.addTrack(track);

		Point p = new Point();
		Waypoint wpt = null;

		for (int i = 0; i < pointRecs; i++) {

			if (i == lapEndPt) {
				trackSeg = track.addTrackseg();
				if (wpt != null) {
					trackSeg.addWaypoint(wpt);
				}
				lapIdx++;
				if (lapIdx < laps.size()) {
					lapEndPt = laps.get(lapIdx).endPt.get();
				}
				trackSeg.setName(String.format("Lap %d", lapIdx));
			}
			p.read(inStream);

			double lat = (double) p.lat.get() / LATLONSCALE;
			double lon = (double) p.lon.get() / LATLONSCALE;
			wpt = new Waypoint(lat, lon);
			wpt.setEle(p.alt.get());
			wpt.setTime(gpsDate.plusMillis(totalTime * 100).toDate());

			addExtIfNotZero(wpt, Const.EXT_SPEED, p.gpsSpeed.get() / 100f);
			addExtIfNotZero(wpt, Const.EXT_HRMHR, p.hrHr.get());
			addExtIfNotZero(wpt, Const.EXT_HRMSPEED, p.speedSpeed.get());
			addExtIfNotZero(wpt, Const.EXT_HRMCADENCE, p.cadCadence.get());
			addExtIfNotZero(wpt, Const.EXT_HRMPOWER, p.pwrPower.get());

			Byte temp = p.temp.get();
			if (temp != -127) {
				addExtIfNotZero(wpt, Const.EXT_HRMTEMP, temp);
			}

			trackSeg.addWaypoint(wpt);
			totalTime += p.intTime.get();
			// System.out.println(String.format("%f %f %d", lat, lon, p.alt.get()));

		}

	}

	/**
	 * add value to {@link Waypoint} sourceFmt if value is not zero.
	 * @param wpt {@link Waypoint} to add sourceFmt to
	 * @param ext sourceFmt's key
	 * @param value value to add
	 */
	private void addExtIfNotZero(Waypoint wpt, String ext, long value) {
		if (value != 0) {
			wpt.getExtension().add(ext, String.format("%d", value));
		}
	}

	/**
	 * add value to {@link Waypoint} sourceFmt if value is not zero.
	 * @param wpt {@link Waypoint} to add sourceFmt to
	 * @param ext sourceFmt's key
	 * @param value value to add
	 */
	private void addExtIfNotZero(Waypoint wpt, String ext, double value) {
		if (value != 0) {
			wpt.getExtension().add(ext, String.format(numLocale, "%.2f", value));
		}
	}

	@Override
	public void loadCumulative(InputStream inStream) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public void save(GPXFile gpx, OutputStream outStream) {
		throw new UnsupportedOperationException();

	}

	public boolean canValidate() {
		return false;
	}

	@Override
	public void validate(InputStream inStream) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() {
		super.clear();
		laps.clear();
	}

	private final int skipBytes(InputStream inStream, int n) throws IOException {
	    int total = 0;
	    int cur = 0;

	    while ((total<n) && ((cur = (int) inStream.skip(n-total)) > 0)) {
	        total += cur;
	    }

	    return total;
	}

}
