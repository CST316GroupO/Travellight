package groupo.travellight.app;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;

public class FileContentProvider extends ContentProvider {

    private static final String CLASS_NAME = "FileContentProvider";
    public static final String AUTHORITY = "groupo.travellight.app.FileContentProvider";
    private UriMatcher uriMatcher;

    @Override
    public boolean onCreate(){
        uriMatcher = new UriMatcher(uriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,"*",1);
        return true;
    }

    @Override
    public Cursor query (Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
        return null;
    }

    @Override
    public Uri insert (Uri uri, ContentValues values){
        return null;
    }

    @Override
    public int update (Uri uri, ContentValues values, String selection, String[] selectionArgs){
        return 0;
    }

    @Override
    public int delete (Uri uri, String selection, String[] selectionArgs){
        return 0;
    }

    @Override
    public String getType (Uri uri){
        return null;
    }

    @Override
    public ParcelFileDescriptor openFile (Uri uri, String mode) throws FileNotFoundException {
        List<String> uriSegments = uri.getPathSegments();
        String secondLastSeg="";
        for (int i=uriSegments.size()-1;i>=0;i--){
            if (uriSegments.get(i).contains("@")){secondLastSeg=uriSegments.get(i);}
        }
        String LOG_TAG = CLASS_NAME + " - openFile";

        Log.v(LOG_TAG,
                "Called with uri: '" + uri + "'." + uri.getLastPathSegment());
        ParcelFileDescriptor pfd;
        String fileLocation = getContext().getFilesDir() + File.separator + secondLastSeg+File.separator+uri.getLastPathSegment().toString();

        pfd = ParcelFileDescriptor.open(new File(
                fileLocation), ParcelFileDescriptor.MODE_READ_ONLY);
        return pfd;
    }
}
