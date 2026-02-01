package com.keresman.flatearth.dao

import android.content.Context
import com.keresman.flatearth.dao.sql.SQLiteRepository



class RepositoryFactory {

    companion object {
        private lateinit var repository: Repository

        fun getInstance(context: Context?): Repository {
            if (!::repository.isInitialized) {
                repository = SQLiteRepository(context)
            }
            return repository
        }
    }
}

/**

public final class RepositoryFactory {
@NotNull
public static final Companion Companion = new Companion((DefaultConstructorMarker)null);
private static Repository repository;

public static final class Companion {
private Companion() {
}

@NotNull
public final Repository getInstance(@Nullable Context context) {
if (RepositoryFactory.repository == null) {
RepositoryFactory.repository = (Repository)(new SQLiteRepository(context));
}

Repository var10000 = RepositoryFactory.repository;
if (var10000 == null) {
Intrinsics.throwUninitializedPropertyAccessException("repository");
var10000 = null;
}

return var10000;
}

public Companion(DefaultConstructorMarker $constructor_marker) {
this();
}
}

 */