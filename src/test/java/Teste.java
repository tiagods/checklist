import com.github.davidmoten.rx.Obs;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Teste {

    @Test
    public void iniciar() {
        StringBuilder result = new StringBuilder();

        Observable.fromArray("book1", "book2")
                .flatMap(s -> getTitle(s))
                .subscribe(result::append,
                        Throwable::printStackTrace,
                        () -> result.append("_completed")
                );

        System.out.println(result);
        Assert.assertTrue(result.toString().equals("book1book2_completed"));
    }

    Observable<String> getTitle(String l) {
        return Observable.just(l);
    }

    @Test
    public void testar(){
        Set<Integer> linhaset = Stream
                .iterate(1, n -> n+1)
                .limit(10-1)
                .collect(Collectors.toSet());
        linhaset.forEach(c->System.out.println(c));
    }

}
