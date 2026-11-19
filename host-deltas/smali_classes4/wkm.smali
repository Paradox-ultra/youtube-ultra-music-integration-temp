.class public final Lwkm;
.super Ljava/lang/Object;
.source "PG"

# interfaces
.implements Lwkj;


# instance fields
.field public final a:Landroid/content/SharedPreferences;

.field public final b:Lbeqo;

.field public final c:Lwkh;

.field public final d:Ljava/util/concurrent/atomic/AtomicReference;

.field public final e:Lbeqo;

.field private final f:Ljava/util/Map;

.field private final g:Z

.field private final h:Lbeqo;


# direct methods
.method public constructor <init>(Landroid/content/SharedPreferences;Lbeqo;Lywj;Lbeqo;Lwkh;Lbeqo;)V
    .locals 0

    .line 1
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    invoke-static {p1}, Lparadox/music/nativeui/AccountBridge;->bindHost(Landroid/content/SharedPreferences;)V

    .line 2
    .line 3
    .line 4
    iput-object p1, p0, Lwkm;->a:Landroid/content/SharedPreferences;

    .line 5
    .line 6
    iput-object p2, p0, Lwkm;->b:Lbeqo;

    .line 7
    .line 8
    iput-object p5, p0, Lwkm;->c:Lwkh;

    .line 9
    .line 10
    iput-object p4, p0, Lwkm;->e:Lbeqo;

    .line 11
    .line 12
    iput-object p6, p0, Lwkm;->h:Lbeqo;

    .line 13
    .line 14
    new-instance p1, Lj$/util/concurrent/ConcurrentHashMap;

    .line 15
    .line 16
    invoke-direct {p1}, Lj$/util/concurrent/ConcurrentHashMap;-><init>()V

    .line 17
    .line 18
    .line 19
    iput-object p1, p0, Lwkm;->f:Ljava/util/Map;

    .line 20
    .line 21
    sget p1, Lywo;->a:I

    .line 22
    .line 23
    const p1, 0x100100f1

    .line 24
    .line 25
    .line 26
    invoke-interface {p3, p1}, Lywj;->d(I)Z

    .line 27
    .line 28
    .line 29
    move-result p1

    .line 30
    iput-boolean p1, p0, Lwkm;->g:Z

    .line 31
    .line 32
    new-instance p1, Ljava/util/concurrent/atomic/AtomicReference;

    .line 33
    .line 34
    invoke-static {}, Lwkl;->c()Lachb;

    .line 35
    .line 36
    .line 37
    move-result-object p2

    .line 38
    invoke-virtual {p2}, Lachb;->f()Lwkl;

    .line 39
    .line 40
    .line 41
    move-result-object p2

    .line 42
    invoke-direct {p1, p2}, Ljava/util/concurrent/atomic/AtomicReference;-><init>(Ljava/lang/Object;)V

    .line 43
    .line 44
    .line 45
    iput-object p1, p0, Lwkm;->d:Ljava/util/concurrent/atomic/AtomicReference;

    .line 46
    .line 47
    return-void
.end method

.method private final G(Ljava/util/function/Predicate;Laghp;Lcom/google/common/collect/ImmutableSet;Lamzp;I)Lj$/util/stream/Stream;
    .locals 1

    .line 1
    if-nez p2, :cond_0

    .line 2
    .line 3
    invoke-virtual {p3}, Lcom/google/common/collect/ImmutableSet;->isEmpty()Z

    .line 4
    .line 5
    .line 6
    move-result v0

    .line 7
    if-eqz v0, :cond_0

    .line 8
    .line 9
    invoke-static {}, Lj$/util/stream/Stream$-CC;->empty()Lj$/util/stream/Stream;

    .line 10
    .line 11
    .line 12
    move-result-object p1

    .line 13
    return-object p1

    .line 14
    :cond_0
    invoke-static {p3}, Lj$/util/Collection$-EL;->stream(Ljava/util/Collection;)Lj$/util/stream/Stream;

    .line 15
    .line 16
    .line 17
    move-result-object p3

    .line 18
    invoke-static {p2}, Lj$/util/stream/Stream$-CC;->ofNullable(Ljava/lang/Object;)Lj$/util/stream/Stream;

    .line 19
    .line 20
    .line 21
    move-result-object p2

    .line 22
    invoke-static {p3, p2}, Lj$/util/stream/Stream$-CC;->concat(Lj$/util/stream/Stream;Lj$/util/stream/Stream;)Lj$/util/stream/Stream;

    .line 23
    .line 24
    .line 25
    move-result-object p2

    .line 26
    new-instance p3, Lvwr;

    .line 27
    .line 28
    const/16 v0, 0x12

    .line 29
    .line 30
    invoke-direct {p3, v0}, Lvwr;-><init>(I)V

    .line 31
    .line 32
    .line 33
    invoke-interface {p2, p3}, Lj$/util/stream/Stream;->filter(Ljava/util/function/Predicate;)Lj$/util/stream/Stream;

    .line 34
    .line 35
    .line 36
    move-result-object p2

    .line 37
    new-instance p3, Lvoy;

    .line 38
    .line 39
    const/16 v0, 0x14

    .line 40
    .line 41
    invoke-direct {p3, p1, v0}, Lvoy;-><init>(Ljava/lang/Object;I)V

    .line 42
    .line 43
    .line 44
    invoke-interface {p2, p3}, Lj$/util/stream/Stream;->filter(Ljava/util/function/Predicate;)Lj$/util/stream/Stream;

    .line 45
    .line 46
    .line 47
    move-result-object p1

    .line 48
    new-instance p2, Lwcw;

    .line 49
    .line 50
    const/16 p3, 0xb

    .line 51
    .line 52
    invoke-direct {p2, p3}, Lwcw;-><init>(I)V

    .line 53
    .line 54
    .line 55
    invoke-interface {p1, p2}, Lj$/util/stream/Stream;->map(Ljava/util/function/Function;)Lj$/util/stream/Stream;

    .line 56
    .line 57
    .line 58
    move-result-object p1

    .line 59
    new-instance p2, Lwko;

    .line 60
    .line 61
    const/4 p3, 0x1

    .line 62
    invoke-direct {p2, p4, p3}, Lwko;-><init>(Ljava/lang/Object;I)V

    .line 63
    .line 64
    .line 65
    invoke-interface {p1, p2}, Lj$/util/stream/Stream;->filter(Ljava/util/function/Predicate;)Lj$/util/stream/Stream;

    .line 66
    .line 67
    .line 68
    move-result-object p1

    .line 69
    new-instance p2, Ljct;

    .line 70
    .line 71
    const/4 p3, 0x5

    .line 72
    invoke-direct {p2, p0, p5, p3}, Ljct;-><init>(Ljava/lang/Object;II)V

    .line 73
    .line 74
    .line 75
    invoke-interface {p1, p2}, Lj$/util/stream/Stream;->map(Ljava/util/function/Function;)Lj$/util/stream/Stream;

    .line 76
    .line 77
    .line 78
    move-result-object p1

    .line 79
    return-object p1
.end method

.method static final z(Lagha;Ljava/lang/String;)V
    .locals 1

    .line 1
    sget-object v0, Laggz;->I:Laggz;

    .line 2
    .line 3
    invoke-static {p0, v0, p1}, Laghb;->a(Lagha;Laggz;Ljava/lang/String;)V

    .line 4
    .line 5
    .line 6
    return-void
.end method


# virtual methods
.method public final A()Lamzp;
    .locals 3

    .line 1
    iget-object v0, p0, Lwkm;->d:Ljava/util/concurrent/atomic/AtomicReference;

    .line 2
    .line 3
    invoke-virtual {v0}, Ljava/util/concurrent/atomic/AtomicReference;->get()Ljava/lang/Object;

    .line 4
    .line 5
    .line 6
    move-result-object v0

    .line 7
    check-cast v0, Lwkl;

    .line 8
    .line 9
    iget-object v1, v0, Lwkl;->b:Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;

    .line 10
    .line 11
    iget-object v0, v0, Lwkl;->a:Lcom/google/common/collect/ImmutableSet;

    .line 12
    .line 13
    invoke-virtual {v0}, Lcom/google/common/collect/ImmutableSet;->isEmpty()Z

    .line 14
    .line 15
    .line 16
    move-result v2

    .line 17
    if-eqz v2, :cond_1

    .line 18
    .line 19
    if-eqz v1, :cond_0

    .line 20
    .line 21
    goto :goto_0

    .line 22
    :cond_0
    sget v0, Lamzp;->d:I

    .line 23
    .line 24
    sget-object v0, Landy;->a:Lamzp;

    .line 25
    .line 26
    return-object v0

    .line 27
    :cond_1
    :goto_0
    invoke-virtual {v0}, Lcom/google/common/collect/ImmutableSet;->isEmpty()Z

    .line 28
    .line 29
    .line 30
    move-result v2

    .line 31
    if-eqz v2, :cond_2

    .line 32
    .line 33
    invoke-virtual {v1}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    .line 34
    .line 35
    .line 36
    new-instance v0, Lanez;

    .line 37
    .line 38
    invoke-direct {v0, v1}, Lanez;-><init>(Ljava/lang/Object;)V

    .line 39
    .line 40
    .line 41
    :cond_2
    invoke-static {v0}, Lj$/util/Collection$-EL;->stream(Ljava/util/Collection;)Lj$/util/stream/Stream;

    .line 42
    .line 43
    .line 44
    move-result-object v0

    .line 45
    new-instance v1, Lvwr;

    .line 46
    .line 47
    const/16 v2, 0x13

    .line 48
    .line 49
    invoke-direct {v1, v2}, Lvwr;-><init>(I)V

    .line 50
    .line 51
    .line 52
    invoke-interface {v0, v1}, Lj$/util/stream/Stream;->filter(Ljava/util/function/Predicate;)Lj$/util/stream/Stream;

    .line 53
    .line 54
    .line 55
    move-result-object v0

    .line 56
    new-instance v1, Lwcw;

    .line 57
    .line 58
    const/16 v2, 0xc

    .line 59
    .line 60
    invoke-direct {v1, v2}, Lwcw;-><init>(I)V

    .line 61
    .line 62
    .line 63
    invoke-interface {v0, v1}, Lj$/util/stream/Stream;->map(Ljava/util/function/Function;)Lj$/util/stream/Stream;

    .line 64
    .line 65
    .line 66
    move-result-object v0

    .line 67
    sget v1, Lamzp;->d:I

    .line 68
    .line 69
    sget-object v1, Lamxc;->a:Lj$/util/stream/Collector;

    .line 70
    .line 71
    invoke-interface {v0, v1}, Lj$/util/stream/Stream;->collect(Lj$/util/stream/Collector;)Ljava/lang/Object;

    .line 72
    .line 73
    .line 74
    move-result-object v0

    .line 75
    check-cast v0, Lamzp;

    .line 76
    .line 77
    return-object v0
.end method

.method public final B(I)V
    .locals 4

    .line 1
    iget-object v0, p0, Lwkm;->h:Lbeqo;

    .line 2
    .line 3
    invoke-interface {v0}, Lbeqo;->a()Ljava/lang/Object;

    .line 4
    .line 5
    .line 6
    move-result-object v0

    .line 7
    check-cast v0, Lbiy;

    .line 8
    .line 9
    sget-object v1, Latfr;->a:Latfr;

    .line 10
    .line 11
    invoke-virtual {v1}, Lapaw;->createBuilder()Lapao;

    .line 12
    .line 13
    .line 14
    move-result-object v1

    .line 15
    check-cast v1, Lapaq;

    .line 16
    .line 17
    sget-object v2, Lapll;->a:Lapll;

    .line 18
    .line 19
    invoke-virtual {v2}, Lapaw;->createBuilder()Lapao;

    .line 20
    .line 21
    .line 22
    move-result-object v2

    .line 23
    invoke-virtual {v2}, Lapao;->copyOnWrite()V

    .line 24
    .line 25
    .line 26
    iget-object v3, v2, Lapao;->instance:Lapaw;

    .line 27
    .line 28
    check-cast v3, Lapll;

    .line 29
    .line 30
    add-int/lit8 p1, p1, -0x1

    .line 31
    .line 32
    iput p1, v3, Lapll;->e:I

    .line 33
    .line 34
    iget p1, v3, Lapll;->b:I

    .line 35
    .line 36
    or-int/lit8 p1, p1, 0x4

    .line 37
    .line 38
    iput p1, v3, Lapll;->b:I

    .line 39
    .line 40
    invoke-virtual {v1}, Lapao;->copyOnWrite()V

    .line 41
    .line 42
    .line 43
    iget-object p1, v1, Lapaq;->instance:Lapaw;

    .line 44
    .line 45
    check-cast p1, Latfr;

    .line 46
    .line 47
    invoke-virtual {v2}, Lapao;->build()Lapaw;

    .line 48
    .line 49
    .line 50
    move-result-object v2

    .line 51
    check-cast v2, Lapll;

    .line 52
    .line 53
    invoke-virtual {v2}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    .line 54
    .line 55
    .line 56
    iput-object v2, p1, Latfr;->d:Ljava/lang/Object;

    .line 57
    .line 58
    const/16 v2, 0x185

    .line 59
    .line 60
    iput v2, p1, Latfr;->c:I

    .line 61
    .line 62
    invoke-virtual {v1}, Lapao;->build()Lapaw;

    .line 63
    .line 64
    .line 65
    move-result-object p1

    .line 66
    check-cast p1, Latfr;

    .line 67
    .line 68
    invoke-virtual {v0, p1}, Lbiy;->G(Latfr;)V

    .line 69
    .line 70
    .line 71
    return-void
.end method

.method public final C()Lamzp;
    .locals 8

    .line 1
    invoke-static {}, Lqxs;->aX()V

    .line 2
    .line 3
    .line 4
    iget-object v0, p0, Lwkm;->d:Ljava/util/concurrent/atomic/AtomicReference;

    .line 5
    .line 6
    iget-object v1, p0, Lwkm;->c:Lwkh;

    .line 7
    .line 8
    invoke-interface {v1}, Lwkh;->d()Lamzp;

    .line 9
    .line 10
    .line 11
    move-result-object v6

    .line 12
    invoke-virtual {v0}, Ljava/util/concurrent/atomic/AtomicReference;->get()Ljava/lang/Object;

    .line 13
    .line 14
    .line 15
    move-result-object v0

    .line 16
    check-cast v0, Lwkl;

    .line 17
    .line 18
    iget-object v4, v0, Lwkl;->b:Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;

    .line 19
    .line 20
    iget-object v5, v0, Lwkl;->a:Lcom/google/common/collect/ImmutableSet;

    .line 21
    .line 22
    if-nez v4, :cond_0

    .line 23
    .line 24
    invoke-virtual {v5}, Lcom/google/common/collect/ImmutableSet;->isEmpty()Z

    .line 25
    .line 26
    .line 27
    move-result v0

    .line 28
    if-eqz v0, :cond_0

    .line 29
    .line 30
    return-object v6

    .line 31
    :cond_0
    sget v0, Lamzp;->d:I

    .line 32
    .line 33
    new-instance v0, Lamzk;

    .line 34
    .line 35
    invoke-direct {v0}, Lamzk;-><init>()V

    .line 36
    .line 37
    .line 38
    invoke-virtual {v0, v6}, Lamzk;->j(Ljava/lang/Iterable;)V

    .line 39
    .line 40
    .line 41
    new-instance v3, Lvwr;

    .line 42
    .line 43
    const/16 v1, 0x11

    .line 44
    .line 45
    invoke-direct {v3, v1}, Lvwr;-><init>(I)V

    .line 46
    .line 47
    .line 48
    const/16 v7, 0x13

    .line 49
    .line 50
    move-object v2, p0

    .line 51
    invoke-direct/range {v2 .. v7}, Lwkm;->G(Ljava/util/function/Predicate;Laghp;Lcom/google/common/collect/ImmutableSet;Lamzp;I)Lj$/util/stream/Stream;

    .line 52
    .line 53
    .line 54
    move-result-object v1

    .line 55
    new-instance v2, Lweb;

    .line 56
    .line 57
    const/4 v3, 0x4

    .line 58
    invoke-direct {v2, v0, v3}, Lweb;-><init>(Ljava/lang/Object;I)V

    .line 59
    .line 60
    .line 61
    invoke-interface {v1, v2}, Lj$/util/stream/Stream;->forEach(Ljava/util/function/Consumer;)V

    .line 62
    .line 63
    .line 64
    invoke-virtual {v0}, Lamzk;->g()Lamzp;

    .line 65
    .line 66
    .line 67
    move-result-object v0

    .line 68
    return-object v0
.end method

.method public final D()Lamzp;
    .locals 8

    .line 1
    invoke-static {}, Lqxs;->aX()V

    .line 2
    .line 3
    .line 4
    iget-object v0, p0, Lwkm;->d:Ljava/util/concurrent/atomic/AtomicReference;

    .line 5
    .line 6
    iget-object v1, p0, Lwkm;->c:Lwkh;

    .line 7
    .line 8
    invoke-interface {v1}, Lwkh;->e()Lamzp;

    .line 9
    .line 10
    .line 11
    move-result-object v6

    .line 12
    invoke-virtual {v0}, Ljava/util/concurrent/atomic/AtomicReference;->get()Ljava/lang/Object;

    .line 13
    .line 14
    .line 15
    move-result-object v0

    .line 16
    check-cast v0, Lwkl;

    .line 17
    .line 18
    iget-object v4, v0, Lwkl;->b:Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;

    .line 19
    .line 20
    iget-object v5, v0, Lwkl;->a:Lcom/google/common/collect/ImmutableSet;

    .line 21
    .line 22
    const/16 v0, 0x14

    .line 23
    .line 24
    if-nez v4, :cond_0

    .line 25
    .line 26
    invoke-virtual {v5}, Lcom/google/common/collect/ImmutableSet;->isEmpty()Z

    .line 27
    .line 28
    .line 29
    move-result v1

    .line 30
    if-eqz v1, :cond_0

    .line 31
    .line 32
    invoke-virtual {p0, v0}, Lwkm;->B(I)V

    .line 33
    .line 34
    .line 35
    return-object v6

    .line 36
    :cond_0
    sget v1, Lamzp;->d:I

    .line 37
    .line 38
    new-instance v1, Lamzk;

    .line 39
    .line 40
    invoke-direct {v1}, Lamzk;-><init>()V

    .line 41
    .line 42
    .line 43
    invoke-virtual {v1, v6}, Lamzk;->j(Ljava/lang/Iterable;)V

    .line 44
    .line 45
    .line 46
    new-instance v3, Lvwr;

    .line 47
    .line 48
    invoke-direct {v3, v0}, Lvwr;-><init>(I)V

    .line 49
    .line 50
    .line 51
    const/16 v7, 0x12

    .line 52
    .line 53
    move-object v2, p0

    .line 54
    invoke-direct/range {v2 .. v7}, Lwkm;->G(Ljava/util/function/Predicate;Laghp;Lcom/google/common/collect/ImmutableSet;Lamzp;I)Lj$/util/stream/Stream;

    .line 55
    .line 56
    .line 57
    move-result-object v0

    .line 58
    new-instance v2, Lweb;

    .line 59
    .line 60
    const/4 v3, 0x4

    .line 61
    invoke-direct {v2, v1, v3}, Lweb;-><init>(Ljava/lang/Object;I)V

    .line 62
    .line 63
    .line 64
    invoke-interface {v0, v2}, Lj$/util/stream/Stream;->forEach(Ljava/util/function/Consumer;)V

    .line 65
    .line 66
    .line 67
    invoke-virtual {v1}, Lamzk;->g()Lamzp;

    .line 68
    .line 69
    .line 70
    move-result-object v0

    .line 71
    return-object v0
.end method

.method public final E(Ljava/lang/String;)Laghp;
    .locals 2

    .line 1
    iget-object v0, p0, Lwkm;->d:Ljava/util/concurrent/atomic/AtomicReference;

    .line 2
    .line 3
    invoke-virtual {v0}, Ljava/util/concurrent/atomic/AtomicReference;->get()Ljava/lang/Object;

    .line 4
    .line 5
    .line 6
    move-result-object v0

    .line 7
    check-cast v0, Lwkl;

    .line 8
    .line 9
    iget-object v0, v0, Lwkl;->b:Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;

    .line 10
    .line 11
    if-eqz v0, :cond_1

    .line 12
    .line 13
    invoke-virtual {v0}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->b()Ljava/lang/String;

    .line 14
    .line 15
    .line 16
    move-result-object v1

    .line 17
    invoke-virtual {v1, p1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    .line 18
    .line 19
    .line 20
    move-result v1

    .line 21
    if-nez v1, :cond_0

    .line 22
    .line 23
    goto :goto_0

    .line 24
    :cond_0
    return-object v0

    .line 25
    :cond_1
    :goto_0
    iget-object v0, p0, Lwkm;->f:Ljava/util/Map;

    .line 26
    .line 27
    invoke-interface {v0, p1}, Ljava/util/Map;->get(Ljava/lang/Object;)Ljava/lang/Object;

    .line 28
    .line 29
    .line 30
    move-result-object v0

    .line 31
    check-cast v0, Laghp;

    .line 32
    .line 33
    if-nez v0, :cond_6

    .line 34
    .line 35
    const-string v0, ""

    .line 36
    .line 37
    invoke-virtual {v0, p1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    .line 38
    .line 39
    .line 40
    move-result v0

    .line 41
    if-eqz v0, :cond_2

    .line 42
    .line 43
    sget-object p1, Lagho;->a:Laghp;

    .line 44
    .line 45
    return-object p1

    .line 46
    :cond_2
    invoke-static {p1}, Lwkq;->c(Ljava/lang/String;)Z

    .line 47
    .line 48
    .line 49
    move-result v0

    .line 50
    if-eqz v0, :cond_3

    .line 51
    .line 52
    invoke-static {p1, p1}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->r(Ljava/lang/String;Ljava/lang/String;)Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;

    .line 53
    .line 54
    .line 55
    move-result-object p1

    .line 56
    return-object p1

    .line 57
    :cond_3
    invoke-static {}, Lqxs;->aZ()Z

    .line 58
    .line 59
    .line 60
    move-result v0

    .line 61
    if-nez v0, :cond_4

    .line 62
    .line 63
    const-string v0, "AbstractIdentityStore getIdentityByDataSyncId() hitting DB on main thread."

    .line 64
    .line 65
    invoke-static {v0}, Lzdn;->n(Ljava/lang/String;)V

    .line 66
    .line 67
    .line 68
    :cond_4
    iget-object v0, p0, Lwkm;->f:Ljava/util/Map;

    .line 69
    .line 70
    invoke-interface {v0, p1}, Ljava/util/Map;->get(Ljava/lang/Object;)Ljava/lang/Object;

    .line 71
    .line 72
    .line 73
    move-result-object v0

    .line 74
    check-cast v0, Laghp;

    .line 75
    .line 76
    if-eqz v0, :cond_5

    .line 77
    .line 78
    return-object v0

    .line 79
    :cond_5
    iget-object v0, p0, Lwkm;->c:Lwkh;

    .line 80
    .line 81
    invoke-interface {v0, p1}, Lwkh;->c(Ljava/lang/String;)Laghp;

    .line 82
    .line 83
    .line 84
    move-result-object v0

    .line 85
    if-eqz v0, :cond_6

    .line 86
    .line 87
    iget-object v1, p0, Lwkm;->f:Ljava/util/Map;

    .line 88
    .line 89
    invoke-interface {v1, p1, v0}, Ljava/util/Map;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    .line 90
    .line 91
    .line 92
    :cond_6
    return-object v0
.end method

.method public final F(Lwkl;Lachb;)Z
    .locals 1

    .line 1
    iget-object v0, p0, Lwkm;->d:Ljava/util/concurrent/atomic/AtomicReference;

    .line 2
    .line 3
    invoke-virtual {p2}, Lachb;->f()Lwkl;

    .line 4
    .line 5
    .line 6
    move-result-object p2

    .line 7
    invoke-static {v0, p1, p2}, La;->ad(Ljava/util/concurrent/atomic/AtomicReference;Ljava/lang/Object;Ljava/lang/Object;)Z

    .line 8
    .line 9
    .line 10
    move-result p1

    .line 11
    return p1
.end method

.method public final a()I
    .locals 3

    .line 1
    iget-object v0, p0, Lwkm;->a:Landroid/content/SharedPreferences;

    .line 2
    .line 3
    const-string v1, "identity_version"

    .line 4
    .line 5
    const/4 v2, 0x2

    .line 6
    invoke-interface {v0, v1, v2}, Landroid/content/SharedPreferences;->getInt(Ljava/lang/String;I)I

    .line 7
    .line 8
    .line 9
    move-result v0

    .line 10
    return v0
.end method

.method public final b()Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;
    .locals 5

    .line 1
    invoke-virtual {p0}, Lwkm;->a()I

    .line 2
    .line 3
    .line 4
    move-result v0

    .line 5
    const/4 v1, 0x0

    .line 6
    const/4 v2, 0x1

    .line 7
    if-ne v0, v2, :cond_1

    .line 8
    .line 9
    iget-object v0, p0, Lwkm;->a:Landroid/content/SharedPreferences;

    .line 10
    .line 11
    const-string v3, "user_account"

    .line 12
    .line 13
    invoke-interface {v0, v3, v1}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    .line 14
    .line 15
    .line 16
    move-result-object v0

    .line 17
    if-eqz v0, :cond_1

    .line 18
    .line 19
    iget-object v3, p0, Lwkm;->a:Landroid/content/SharedPreferences;

    .line 20
    .line 21
    const-string v4, "user_identity"

    .line 22
    .line 23
    invoke-interface {v3, v4, v1}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    .line 24
    .line 25
    .line 26
    move-result-object v1

    .line 27
    invoke-static {v0, v1}, Lwkq;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    .line 28
    .line 29
    .line 30
    move-result-object v3

    .line 31
    const-string v4, "No +Page Delegate"

    .line 32
    .line 33
    invoke-static {v1, v4}, Lj$/util/Objects;->equals(Ljava/lang/Object;Ljava/lang/Object;)Z

    .line 34
    .line 35
    .line 36
    move-result v4

    .line 37
    if-ne v2, v4, :cond_0

    .line 38
    .line 39
    const-string v1, ""

    .line 40
    .line 41
    :cond_0
    invoke-static {v3, v0, v1, v3}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->n(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;

    .line 42
    .line 43
    .line 44
    move-result-object v0

    .line 45
    return-object v0

    .line 46
    :cond_1
    return-object v1
.end method

.method public final c(Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;)Lcom/google/common/util/concurrent/ListenableFuture;
    .locals 3

    .line 1
    iget-object v0, p0, Lwkm;->c:Lwkh;

    .line 2
    .line 3
    invoke-interface {v0, p1}, Lwkh;->f(Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;)Lcom/google/common/util/concurrent/ListenableFuture;

    .line 4
    .line 5
    .line 6
    move-result-object v0

    .line 7
    invoke-static {v0}, Lamlm;->d(Lcom/google/common/util/concurrent/ListenableFuture;)Lamlm;

    .line 8
    .line 9
    .line 10
    move-result-object v0

    .line 11
    new-instance v1, Luxr;

    .line 12
    .line 13
    const/4 v2, 0x6

    .line 14
    invoke-direct {v1, p0, p1, v2}, Luxr;-><init>(Ljava/lang/Object;Ljava/lang/Object;I)V

    .line 15
    .line 16
    .line 17
    sget-object p1, Lansq;->a:Lansq;

    .line 18
    .line 19
    invoke-virtual {v0, v1, p1}, Lamlm;->h(Lansb;Ljava/util/concurrent/Executor;)Lamlm;

    .line 20
    .line 21
    .line 22
    move-result-object p1

    .line 23
    new-instance v0, Lulo;

    .line 24
    .line 25
    const/16 v1, 0xc

    .line 26
    .line 27
    invoke-direct {v0, v1}, Lulo;-><init>(I)V

    .line 28
    .line 29
    .line 30
    sget-object v1, Lansq;->a:Lansq;

    .line 31
    .line 32
    invoke-virtual {p1, v0, v1}, Lamlm;->g(Lamtm;Ljava/util/concurrent/Executor;)Lamlm;

    .line 33
    .line 34
    .line 35
    move-result-object p1

    .line 36
    new-instance v0, Lulo;

    .line 37
    .line 38
    const/16 v1, 0xd

    .line 39
    .line 40
    invoke-direct {v0, v1}, Lulo;-><init>(I)V

    .line 41
    .line 42
    .line 43
    sget-object v1, Lansq;->a:Lansq;

    .line 44
    .line 45
    const-class v2, Ljava/lang/IllegalStateException;

    .line 46
    .line 47
    invoke-virtual {p1, v2, v0, v1}, Lamlm;->b(Ljava/lang/Class;Lamtm;Ljava/util/concurrent/Executor;)Lamlm;

    .line 48
    .line 49
    .line 50
    move-result-object p1

    .line 51
    return-object p1
.end method

.method public final d()V
    .locals 16

    .line 1
    move-object/from16 v0, p0

    .line 2
    .line 3
    iget-object v1, v0, Lwkm;->a:Landroid/content/SharedPreferences;

    .line 4
    .line 5
    const-string v2, "user_account"

    .line 6
    .line 7
    const/4 v3, 0x0

    .line 8
    invoke-interface {v1, v2, v3}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    .line 9
    .line 10
    .line 11
    move-result-object v2

    .line 12
    const-string v4, "user_identity_id"

    .line 13
    .line 14
    invoke-interface {v1, v4, v3}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    .line 15
    .line 16
    .line 17
    move-result-object v4

    .line 18
    const-string v5, "datasync_id"

    .line 19
    .line 20
    const-string v6, ""

    .line 21
    .line 22
    invoke-interface {v1, v5, v6}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    .line 23
    .line 24
    .line 25
    move-result-object v5

    .line 26
    const-string v7, "IS_INCOGNITO_SESSION_IDENTITY"

    .line 27
    .line 28
    const/4 v8, 0x0

    .line 29
    invoke-interface {v1, v7, v8}, Landroid/content/SharedPreferences;->getBoolean(Ljava/lang/String;Z)Z

    .line 30
    .line 31
    .line 32
    move-result v7

    .line 33
    const-string v9, "persona_account"

    .line 34
    .line 35
    invoke-interface {v1, v9, v8}, Landroid/content/SharedPreferences;->getBoolean(Ljava/lang/String;Z)Z

    .line 36
    .line 37
    .line 38
    move-result v9

    .line 39
    const-string v10, "IS_UNICORN_CHILD_ACCOUNT"

    .line 40
    .line 41
    invoke-interface {v1, v10, v8}, Landroid/content/SharedPreferences;->getBoolean(Ljava/lang/String;Z)Z

    .line 42
    .line 43
    .line 44
    move-result v10

    .line 45
    const-string v11, "HAS_GRIFFIN_POLICY"

    .line 46
    .line 47
    invoke-interface {v1, v11, v8}, Landroid/content/SharedPreferences;->getBoolean(Ljava/lang/String;Z)Z

    .line 48
    .line 49
    .line 50
    move-result v11

    .line 51
    const-string v12, "IS_CHILD_ACCOUNT_OVER_13"

    .line 52
    .line 53
    invoke-interface {v1, v12, v8}, Landroid/content/SharedPreferences;->getBoolean(Ljava/lang/String;Z)Z

    .line 54
    .line 55
    .line 56
    move-result v8

    .line 57
    const-string v12, "delegation_type"

    .line 58
    .line 59
    const/4 v13, 0x1

    .line 60
    invoke-interface {v1, v12, v13}, Landroid/content/SharedPreferences;->getInt(Ljava/lang/String;I)I

    .line 61
    .line 62
    .line 63
    move-result v1

    .line 64
    invoke-static {v1}, La;->bX(I)I

    .line 65
    .line 66
    .line 67
    move-result v1

    .line 68
    if-nez v1, :cond_0

    .line 69
    .line 70
    const/4 v1, 0x2

    .line 71
    :cond_0
    iget-object v12, v0, Lwkm;->a:Landroid/content/SharedPreferences;

    .line 72
    .line 73
    const-string v14, "user_identity"

    .line 74
    .line 75
    invoke-interface {v12, v14, v3}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    .line 76
    .line 77
    .line 78
    move-result-object v12

    .line 79
    iget-object v14, v0, Lwkm;->a:Landroid/content/SharedPreferences;

    .line 80
    .line 81
    const-string v15, "delegation_context"

    .line 82
    .line 83
    const-string v3, "NO_DELEGATION_CONTEXT"

    .line 84
    .line 85
    invoke-interface {v14, v15, v3}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    .line 86
    .line 87
    .line 88
    move-result-object v14

    .line 89
    const-string v15, "No +Page Delegate"

    .line 90
    .line 91
    invoke-static {v12, v15}, Lj$/util/Objects;->equals(Ljava/lang/Object;Ljava/lang/Object;)Z

    .line 92
    .line 93
    .line 94
    move-result v15

    .line 95
    if-ne v13, v15, :cond_1

    .line 96
    .line 97
    move-object v12, v6

    .line 98
    :cond_1
    invoke-static {v5, v6}, Lj$/util/Objects;->equals(Ljava/lang/Object;Ljava/lang/Object;)Z

    .line 99
    .line 100
    .line 101
    move-result v6

    .line 102
    if-eqz v6, :cond_2

    .line 103
    .line 104
    if-eqz v4, :cond_2

    .line 105
    .line 106
    sget-object v5, Lagha;->b:Lagha;

    .line 107
    .line 108
    const-string v6, "Data sync id is empty"

    .line 109
    .line 110
    invoke-virtual {v0, v5, v6}, Lwkm;->s(Lagha;Ljava/lang/String;)V

    .line 111
    .line 112
    .line 113
    sget-object v5, Lagha;->b:Lagha;

    .line 114
    .line 115
    const-string v6, "[Clockwork][Database]Dropping pref acct w/ empty datasync id"

    .line 116
    .line 117
    invoke-static {v5, v6}, Lwkm;->z(Lagha;Ljava/lang/String;)V

    .line 118
    .line 119
    .line 120
    move-object v5, v4

    .line 121
    :cond_2
    if-nez v7, :cond_3

    .line 122
    .line 123
    invoke-virtual {v0}, Lwkm;->x()Z

    .line 124
    .line 125
    .line 126
    move-result v6

    .line 127
    if-eqz v6, :cond_3

    .line 128
    .line 129
    invoke-virtual {v0}, Lwkm;->e()Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;

    .line 130
    .line 131
    .line 132
    move-result-object v1

    .line 133
    invoke-virtual {v0, v1}, Lwkm;->k(Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;)Lcom/google/common/util/concurrent/ListenableFuture;

    .line 134
    .line 135
    .line 136
    goto :goto_0

    .line 137
    :cond_3
    if-eqz v2, :cond_b

    .line 138
    .line 139
    if-eqz v4, :cond_b

    .line 140
    .line 141
    if-eqz v7, :cond_4

    .line 142
    .line 143
    invoke-static {v4, v5}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->r(Ljava/lang/String;Ljava/lang/String;)Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;

    .line 144
    .line 145
    .line 146
    move-result-object v1

    .line 147
    goto :goto_0

    .line 148
    :cond_4
    if-eqz v9, :cond_5

    .line 149
    .line 150
    invoke-static {v4, v2, v5}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->s(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;

    .line 151
    .line 152
    .line 153
    move-result-object v1

    .line 154
    goto :goto_0

    .line 155
    :cond_5
    const/4 v6, 0x3

    .line 156
    if-eqz v10, :cond_7

    .line 157
    .line 158
    if-ne v1, v6, :cond_6

    .line 159
    .line 160
    invoke-static {v4, v2, v5}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->p(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;

    .line 161
    .line 162
    .line 163
    move-result-object v1

    .line 164
    goto :goto_0

    .line 165
    :cond_6
    invoke-static {v4, v2, v5, v8}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->u(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;

    .line 166
    .line 167
    .line 168
    move-result-object v1

    .line 169
    goto :goto_0

    .line 170
    :cond_7
    if-eqz v11, :cond_9

    .line 171
    .line 172
    if-ne v1, v6, :cond_8

    .line 173
    .line 174
    invoke-static {v4, v2, v5}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->o(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;

    .line 175
    .line 176
    .line 177
    move-result-object v1

    .line 178
    goto :goto_0

    .line 179
    :cond_8
    invoke-static {v4, v2, v5, v8}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->q(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;

    .line 180
    .line 181
    .line 182
    move-result-object v1

    .line 183
    goto :goto_0

    .line 184
    :cond_9
    invoke-static {v14, v3}, Lj$/util/Objects;->equals(Ljava/lang/Object;Ljava/lang/Object;)Z

    .line 185
    .line 186
    .line 187
    move-result v3

    .line 188
    if-nez v3, :cond_a

    .line 189
    .line 190
    invoke-static {v14}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    .line 191
    .line 192
    .line 193
    move-result v3

    .line 194
    if-nez v3, :cond_a

    .line 195
    .line 196
    invoke-static {v4, v2, v5, v1, v14}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->B(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;)Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;

    .line 197
    .line 198
    .line 199
    move-result-object v1

    .line 200
    goto :goto_0

    .line 201
    :cond_a
    invoke-static {v4, v2, v12, v5}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->n(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;

    .line 202
    .line 203
    .line 204
    move-result-object v1

    .line 205
    goto :goto_0

    .line 206
    :cond_b
    const/4 v1, 0x0

    .line 207
    :goto_0
    iget-object v2, v0, Lwkm;->d:Ljava/util/concurrent/atomic/AtomicReference;

    .line 208
    .line 209
    invoke-static {}, Lwkl;->c()Lachb;

    .line 210
    .line 211
    .line 212
    move-result-object v3

    .line 213
    iput-object v1, v3, Lachb;->e:Ljava/lang/Object;

    .line 214
    .line 215
    const/4 v1, 0x0

    .line 216
    iput-object v1, v3, Lachb;->a:Ljava/lang/Object;

    .line 217
    .line 218
    invoke-virtual {v3}, Lachb;->f()Lwkl;

    .line 219
    .line 220
    .line 221
    move-result-object v1

    .line 222
    invoke-virtual {v2, v1}, Ljava/util/concurrent/atomic/AtomicReference;->set(Ljava/lang/Object;)V

    .line 223
    .line 224
    .line 225
    return-void
.end method

.method public final e()Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;
    .locals 4

    .line 1
    iget-object v0, p0, Lwkm;->a:Landroid/content/SharedPreferences;

    .line 2
    .line 3
    const/4 v1, 0x0

    .line 4
    const-string v2, "NEXT_INCOGNITO_SESSION_INDEX"

    .line 5
    .line 6
    invoke-interface {v0, v2, v1}, Landroid/content/SharedPreferences;->getInt(Ljava/lang/String;I)I

    .line 7
    .line 8
    .line 9
    move-result v0

    .line 10
    invoke-static {v0}, Lwkq;->b(I)Ljava/lang/String;

    .line 11
    .line 12
    .line 13
    move-result-object v1

    .line 14
    :goto_0
    iget-object v3, p0, Lwkm;->c:Lwkh;

    .line 15
    .line 16
    invoke-interface {v3, v1}, Lwkh;->b(Ljava/lang/String;)Laghp;

    .line 17
    .line 18
    .line 19
    move-result-object v3

    .line 20
    add-int/lit8 v0, v0, 0x1

    .line 21
    .line 22
    if-eqz v3, :cond_0

    .line 23
    .line 24
    invoke-static {v0}, Lwkq;->b(I)Ljava/lang/String;

    .line 25
    .line 26
    .line 27
    move-result-object v1

    .line 28
    goto :goto_0

    .line 29
    :cond_0
    iget-object v3, p0, Lwkm;->a:Landroid/content/SharedPreferences;

    .line 30
    .line 31
    invoke-interface {v3}, Landroid/content/SharedPreferences;->edit()Landroid/content/SharedPreferences$Editor;

    .line 32
    .line 33
    .line 34
    move-result-object v3

    .line 35
    invoke-interface {v3, v2, v0}, Landroid/content/SharedPreferences$Editor;->putInt(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;

    .line 36
    .line 37
    .line 38
    move-result-object v0

    .line 39
    invoke-interface {v0}, Landroid/content/SharedPreferences$Editor;->apply()V

    .line 40
    .line 41
    .line 42
    invoke-static {v1, v1}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->r(Ljava/lang/String;Ljava/lang/String;)Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;

    .line 43
    .line 44
    .line 45
    move-result-object v0

    .line 46
    return-object v0
.end method

.method public final f()Lwkw;
    .locals 4

    .line 1
    const/4 v0, 0x0

    .line 2
    :cond_0
    iget-object v1, p0, Lwkm;->d:Ljava/util/concurrent/atomic/AtomicReference;

    .line 3
    .line 4
    invoke-virtual {v1}, Ljava/util/concurrent/atomic/AtomicReference;->get()Ljava/lang/Object;

    .line 5
    .line 6
    .line 7
    move-result-object v1

    .line 8
    check-cast v1, Lwkl;

    .line 9
    .line 10
    iget-object v2, v1, Lwkl;->c:Lwkw;

    .line 11
    .line 12
    if-eqz v2, :cond_1

    .line 13
    .line 14
    return-object v2

    .line 15
    :cond_1
    iget-object v3, v1, Lwkl;->b:Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;

    .line 16
    .line 17
    if-eq v0, v3, :cond_2

    .line 18
    .line 19
    invoke-virtual {v3}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    .line 20
    .line 21
    .line 22
    iget-object v0, p0, Lwkm;->c:Lwkh;

    .line 23
    .line 24
    invoke-interface {v0, v3}, Lwkh;->a(Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;)Lwkw;

    .line 25
    .line 26
    .line 27
    move-result-object v2

    .line 28
    move-object v0, v3

    .line 29
    :cond_2
    if-nez v2, :cond_3

    .line 30
    .line 31
    sget-object v2, Lwkw;->a:Lwkw;

    .line 32
    .line 33
    :cond_3
    new-instance v3, Lachb;

    .line 34
    .line 35
    invoke-direct {v3, v1}, Lachb;-><init>(Lwkl;)V

    .line 36
    .line 37
    .line 38
    iput-object v2, v3, Lachb;->a:Ljava/lang/Object;

    .line 39
    .line 40
    invoke-virtual {p0, v1, v3}, Lwkm;->F(Lwkl;Lachb;)Z

    .line 41
    .line 42
    .line 43
    move-result v1

    .line 44
    if-eqz v1, :cond_0

    .line 45
    .line 46
    return-object v2
.end method

.method public final g(Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;)Lwkw;
    .locals 1

    .line 1
    iget-object v0, p0, Lwkm;->c:Lwkh;

    .line 2
    .line 3
    invoke-interface {v0, p1}, Lwkh;->a(Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;)Lwkw;

    .line 4
    .line 5
    .line 6
    move-result-object p1

    .line 7
    return-object p1
.end method

.method public final h()Laghp;
    .locals 1

    .line 1
    iget-object v0, p0, Lwkm;->d:Ljava/util/concurrent/atomic/AtomicReference;

    .line 2
    .line 3
    invoke-virtual {v0}, Ljava/util/concurrent/atomic/AtomicReference;->get()Ljava/lang/Object;

    .line 4
    .line 5
    .line 6
    move-result-object v0

    .line 7
    check-cast v0, Lwkl;

    .line 8
    .line 9
    invoke-virtual {v0}, Lwkl;->a()Laghp;

    .line 10
    .line 11
    .line 12
    move-result-object v0

    .line 13
    return-object v0
.end method

.method public final i(Ljava/lang/String;)Laghp;
    .locals 2

    .line 1
    invoke-static {}, Lqxs;->aX()V

    .line 2
    .line 3
    .line 4
    const-string v0, ""

    .line 5
    .line 6
    invoke-virtual {v0, p1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    .line 7
    .line 8
    .line 9
    move-result v0

    .line 10
    if-eqz v0, :cond_0

    .line 11
    .line 12
    sget-object p1, Lagho;->a:Laghp;

    .line 13
    .line 14
    return-object p1

    .line 15
    :cond_0
    iget-object v0, p0, Lwkm;->d:Ljava/util/concurrent/atomic/AtomicReference;

    .line 16
    .line 17
    invoke-virtual {v0}, Ljava/util/concurrent/atomic/AtomicReference;->get()Ljava/lang/Object;

    .line 18
    .line 19
    .line 20
    move-result-object v0

    .line 21
    check-cast v0, Lwkl;

    .line 22
    .line 23
    iget-object v0, v0, Lwkl;->b:Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;

    .line 24
    .line 25
    if-eqz v0, :cond_2

    .line 26
    .line 27
    invoke-virtual {v0}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->d()Ljava/lang/String;

    .line 28
    .line 29
    .line 30
    move-result-object v1

    .line 31
    invoke-virtual {v1, p1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    .line 32
    .line 33
    .line 34
    move-result v1

    .line 35
    if-nez v1, :cond_1

    .line 36
    .line 37
    goto :goto_0

    .line 38
    :cond_1
    return-object v0

    .line 39
    :cond_2
    :goto_0
    invoke-static {p1}, Lwkq;->c(Ljava/lang/String;)Z

    .line 40
    .line 41
    .line 42
    move-result v0

    .line 43
    if-eqz v0, :cond_3

    .line 44
    .line 45
    invoke-static {p1, p1}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->r(Ljava/lang/String;Ljava/lang/String;)Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;

    .line 46
    .line 47
    .line 48
    move-result-object p1

    .line 49
    return-object p1

    .line 50
    :cond_3
    iget-object v0, p0, Lwkm;->c:Lwkh;

    .line 51
    .line 52
    invoke-interface {v0, p1}, Lwkh;->b(Ljava/lang/String;)Laghp;

    .line 53
    .line 54
    .line 55
    move-result-object p1

    .line 56
    return-object p1
.end method

.method public final j()Lcom/google/common/util/concurrent/ListenableFuture;
    .locals 4

    .line 1
    iget-object v0, p0, Lwkm;->b:Lbeqo;

    .line 2
    .line 3
    invoke-interface {v0}, Lbeqo;->a()Ljava/lang/Object;

    .line 4
    .line 5
    .line 6
    move-result-object v0

    .line 7
    check-cast v0, Lxls;

    .line 8
    .line 9
    invoke-virtual {v0}, Lxls;->k()Lcom/google/common/util/concurrent/ListenableFuture;

    .line 10
    .line 11
    .line 12
    move-result-object v0

    .line 13
    invoke-static {v0}, Lamlm;->d(Lcom/google/common/util/concurrent/ListenableFuture;)Lamlm;

    .line 14
    .line 15
    .line 16
    move-result-object v0

    .line 17
    new-instance v1, Lvfi;

    .line 18
    .line 19
    const/16 v2, 0xc

    .line 20
    .line 21
    invoke-direct {v1, p0, v2}, Lvfi;-><init>(Ljava/lang/Object;I)V

    .line 22
    .line 23
    .line 24
    sget-object v2, Lansq;->a:Lansq;

    .line 25
    .line 26
    invoke-virtual {v0, v1, v2}, Lamlm;->g(Lamtm;Ljava/util/concurrent/Executor;)Lamlm;

    .line 27
    .line 28
    .line 29
    move-result-object v0

    .line 30
    new-instance v1, Lvfi;

    .line 31
    .line 32
    const/16 v2, 0xd

    .line 33
    .line 34
    invoke-direct {v1, p0, v2}, Lvfi;-><init>(Ljava/lang/Object;I)V

    .line 35
    .line 36
    .line 37
    sget-object v2, Lansq;->a:Lansq;

    .line 38
    .line 39
    const-class v3, Ljava/lang/Throwable;

    .line 40
    .line 41
    invoke-virtual {v0, v3, v1, v2}, Lamlm;->b(Ljava/lang/Class;Lamtm;Ljava/util/concurrent/Executor;)Lamlm;

    .line 42
    .line 43
    .line 44
    move-result-object v0

    .line 45
    new-instance v1, Ltcw;

    .line 46
    .line 47
    const/16 v2, 0x12

    .line 48
    .line 49
    invoke-direct {v1, p0, v2}, Ltcw;-><init>(Ljava/lang/Object;I)V

    .line 50
    .line 51
    .line 52
    sget-object v2, Lansq;->a:Lansq;

    .line 53
    .line 54
    invoke-virtual {v0, v1, v2}, Lamlm;->h(Lansb;Ljava/util/concurrent/Executor;)Lamlm;

    .line 55
    .line 56
    .line 57
    move-result-object v0

    .line 58
    invoke-static {v0}, Laorz;->C(Lcom/google/common/util/concurrent/ListenableFuture;)Lcom/google/common/util/concurrent/ListenableFuture;

    .line 59
    .line 60
    .line 61
    move-result-object v0

    .line 62
    return-object v0
.end method

.method public final k(Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;)Lcom/google/common/util/concurrent/ListenableFuture;
    .locals 1

    .line 1
    const/4 v0, 0x0

    .line 2
    invoke-virtual {p0, p1, v0}, Lwkm;->l(Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;Z)Lcom/google/common/util/concurrent/ListenableFuture;

    .line 3
    .line 4
    .line 5
    move-result-object p1

    .line 6
    return-object p1
.end method

.method public final l(Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;Z)Lcom/google/common/util/concurrent/ListenableFuture;
    .locals 12

    .line 1
    iget-object v0, p0, Lwkm;->a:Landroid/content/SharedPreferences;

    .line 2
    .line 3
    invoke-interface {v0}, Landroid/content/SharedPreferences;->edit()Landroid/content/SharedPreferences$Editor;

    .line 4
    .line 5
    .line 6
    move-result-object v0

    .line 7
    const-string v1, "identity_version"

    .line 8
    .line 9
    const/4 v2, 0x2

    .line 10
    invoke-interface {v0, v1, v2}, Landroid/content/SharedPreferences$Editor;->putInt(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;

    .line 11
    .line 12
    .line 13
    move-result-object v0

    .line 14
    const-string v1, "user_signed_out"

    .line 15
    .line 16
    const-string v2, "delegation_context"

    .line 17
    .line 18
    const-string v3, "delegation_type"

    .line 19
    .line 20
    const-string v4, "IS_CHILD_ACCOUNT_OVER_13"

    .line 21
    .line 22
    const-string v5, "HAS_GRIFFIN_POLICY"

    .line 23
    .line 24
    const-string v6, "IS_UNICORN_CHILD_ACCOUNT"

    .line 25
    .line 26
    const-string v7, "datasync_id"

    .line 27
    .line 28
    const-string v8, "user_identity_id"

    .line 29
    .line 30
    const-string v9, "persona_account"

    .line 31
    .line 32
    const-string v10, "user_identity"

    .line 33
    .line 34
    const-string v11, "user_account"

    .line 35
    .line 36
    if-nez p1, :cond_0

    .line 37
    .line 38
    invoke-interface {v0, v11}, Landroid/content/SharedPreferences$Editor;->remove(Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;

    .line 39
    .line 40
    .line 41
    move-result-object v11

    .line 42
    invoke-interface {v11, v10}, Landroid/content/SharedPreferences$Editor;->remove(Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;

    .line 43
    .line 44
    .line 45
    move-result-object v10

    .line 46
    invoke-interface {v10, v9}, Landroid/content/SharedPreferences$Editor;->remove(Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;

    .line 47
    .line 48
    .line 49
    move-result-object v9

    .line 50
    invoke-interface {v9, v8}, Landroid/content/SharedPreferences$Editor;->remove(Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;

    .line 51
    .line 52
    .line 53
    move-result-object v8

    .line 54
    const-string v9, "username"

    .line 55
    .line 56
    invoke-interface {v8, v9}, Landroid/content/SharedPreferences$Editor;->remove(Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;

    .line 57
    .line 58
    .line 59
    move-result-object v8

    .line 60
    invoke-interface {v8, v7}, Landroid/content/SharedPreferences$Editor;->remove(Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;

    .line 61
    .line 62
    .line 63
    move-result-object v7

    .line 64
    invoke-interface {v7, v6}, Landroid/content/SharedPreferences$Editor;->remove(Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;

    .line 65
    .line 66
    .line 67
    move-result-object v6

    .line 68
    invoke-interface {v6, v5}, Landroid/content/SharedPreferences$Editor;->remove(Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;

    .line 69
    .line 70
    .line 71
    move-result-object v5

    .line 72
    invoke-interface {v5, v4}, Landroid/content/SharedPreferences$Editor;->remove(Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;

    .line 73
    .line 74
    .line 75
    move-result-object v4

    .line 76
    invoke-interface {v4, v3}, Landroid/content/SharedPreferences$Editor;->remove(Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;

    .line 77
    .line 78
    .line 79
    move-result-object v3

    .line 80
    invoke-interface {v3, v2}, Landroid/content/SharedPreferences$Editor;->remove(Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;

    .line 81
    .line 82
    .line 83
    move-result-object v2

    .line 84
    invoke-interface {v2, v1, p2}, Landroid/content/SharedPreferences$Editor;->putBoolean(Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;

    .line 85
    .line 86
    .line 87
    goto/16 :goto_0

    .line 88
    .line 89
    :cond_0
    invoke-virtual {p1}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->a()Ljava/lang/String;

    .line 90
    .line 91
    .line 92
    move-result-object p2

    .line 93
    invoke-interface {v0, v11, p2}, Landroid/content/SharedPreferences$Editor;->putString(Ljava/lang/String;Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;

    .line 94
    .line 95
    .line 96
    move-result-object p2

    .line 97
    invoke-virtual {p1}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->e()Ljava/lang/String;

    .line 98
    .line 99
    .line 100
    move-result-object v11

    .line 101
    invoke-interface {p2, v10, v11}, Landroid/content/SharedPreferences$Editor;->putString(Ljava/lang/String;Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;

    .line 102
    .line 103
    .line 104
    move-result-object p2

    .line 105
    invoke-virtual {p1}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->h()Z

    .line 106
    .line 107
    .line 108
    move-result v10

    .line 109
    invoke-interface {p2, v9, v10}, Landroid/content/SharedPreferences$Editor;->putBoolean(Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;

    .line 110
    .line 111
    .line 112
    move-result-object p2

    .line 113
    const-string v9, "IS_INCOGNITO_SESSION_IDENTITY"

    .line 114
    .line 115
    invoke-virtual {p1}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->g()Z

    .line 116
    .line 117
    .line 118
    move-result v10

    .line 119
    invoke-interface {p2, v9, v10}, Landroid/content/SharedPreferences$Editor;->putBoolean(Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;

    .line 120
    .line 121
    .line 122
    move-result-object p2

    .line 123
    invoke-virtual {p1}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->d()Ljava/lang/String;

    .line 124
    .line 125
    .line 126
    move-result-object v9

    .line 127
    invoke-interface {p2, v8, v9}, Landroid/content/SharedPreferences$Editor;->putString(Ljava/lang/String;Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;

    .line 128
    .line 129
    .line 130
    move-result-object p2

    .line 131
    invoke-virtual {p1}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->b()Ljava/lang/String;

    .line 132
    .line 133
    .line 134
    move-result-object v8

    .line 135
    invoke-interface {p2, v7, v8}, Landroid/content/SharedPreferences$Editor;->putString(Ljava/lang/String;Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;

    .line 136
    .line 137
    .line 138
    move-result-object p2

    .line 139
    invoke-virtual {p1}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->j()Z

    .line 140
    .line 141
    .line 142
    move-result v7

    .line 143
    invoke-interface {p2, v6, v7}, Landroid/content/SharedPreferences$Editor;->putBoolean(Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;

    .line 144
    .line 145
    .line 146
    move-result-object p2

    .line 147
    invoke-virtual {p1}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->f()Z

    .line 148
    .line 149
    .line 150
    move-result v6

    .line 151
    invoke-interface {p2, v5, v6}, Landroid/content/SharedPreferences$Editor;->putBoolean(Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;

    .line 152
    .line 153
    .line 154
    move-result-object p2

    .line 155
    invoke-virtual {p1}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->i()Z

    .line 156
    .line 157
    .line 158
    move-result v5

    .line 159
    invoke-interface {p2, v4, v5}, Landroid/content/SharedPreferences$Editor;->putBoolean(Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;

    .line 160
    .line 161
    .line 162
    move-result-object p2

    .line 163
    invoke-virtual {p1}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->l()I

    .line 164
    .line 165
    .line 166
    move-result v4

    .line 167
    add-int/lit8 v4, v4, -0x1

    .line 168
    .line 169
    invoke-interface {p2, v3, v4}, Landroid/content/SharedPreferences$Editor;->putInt(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;

    .line 170
    .line 171
    .line 172
    move-result-object p2

    .line 173
    invoke-virtual {p1}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->c()Ljava/lang/String;

    .line 174
    .line 175
    .line 176
    move-result-object v3

    .line 177
    invoke-interface {p2, v2, v3}, Landroid/content/SharedPreferences$Editor;->putString(Ljava/lang/String;Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;

    .line 178
    .line 179
    .line 180
    invoke-virtual {p1}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->g()Z

    .line 181
    .line 182
    .line 183
    move-result p2

    .line 184
    if-nez p2, :cond_1

    .line 185
    .line 186
    const/4 p2, 0x0

    .line 187
    invoke-interface {v0, v1, p2}, Landroid/content/SharedPreferences$Editor;->putBoolean(Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;

    .line 188
    .line 189
    .line 190
    move-result-object p2

    .line 191
    const-string v1, "incognito_visitor_id"

    .line 192
    .line 193
    invoke-interface {p2, v1}, Landroid/content/SharedPreferences$Editor;->remove(Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;

    .line 194
    .line 195
    .line 196
    iget-object p2, p0, Lwkm;->b:Lbeqo;

    .line 197
    .line 198
    invoke-interface {p2}, Lbeqo;->a()Ljava/lang/Object;

    .line 199
    .line 200
    .line 201
    move-result-object p2

    .line 202
    check-cast p2, Lxls;

    .line 203
    .line 204
    invoke-virtual {p2}, Lxls;->h()Lcom/google/common/util/concurrent/ListenableFuture;

    .line 205
    .line 206
    .line 207
    move-result-object p2

    .line 208
    new-instance v1, Lidv;

    .line 209
    .line 210
    const/4 v2, 0x7

    .line 211
    invoke-direct {v1, v2}, Lidv;-><init>(I)V

    .line 212
    .line 213
    .line 214
    invoke-static {p2, v1}, Lygz;->i(Lcom/google/common/util/concurrent/ListenableFuture;Lygy;)V

    .line 215
    .line 216
    .line 217
    :cond_1
    :goto_0
    invoke-interface {v0}, Landroid/content/SharedPreferences$Editor;->apply()V

    .line 218
    .line 219
    .line 220
    if-eqz p1, :cond_5

    .line 221
    .line 222
    invoke-virtual {p1}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->d()Ljava/lang/String;

    .line 223
    .line 224
    .line 225
    move-result-object p2

    .line 226
    invoke-static {p2}, Lzey;->k(Ljava/lang/String;)V

    .line 227
    .line 228
    .line 229
    invoke-virtual {p1}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->a()Ljava/lang/String;

    .line 230
    .line 231
    .line 232
    move-result-object p2

    .line 233
    invoke-static {p2}, Lzey;->k(Ljava/lang/String;)V

    .line 234
    .line 235
    .line 236
    iget-object p2, p0, Lwkm;->c:Lwkh;

    .line 237
    .line 238
    invoke-interface {p2, p1}, Lwkh;->f(Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;)Lcom/google/common/util/concurrent/ListenableFuture;

    .line 239
    .line 240
    .line 241
    invoke-virtual {p1}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->g()Z

    .line 242
    .line 243
    .line 244
    move-result p2

    .line 245
    if-eqz p2, :cond_2

    .line 246
    .line 247
    goto :goto_1

    .line 248
    :cond_2
    iget-object p2, p0, Lwkm;->f:Ljava/util/Map;

    .line 249
    .line 250
    invoke-virtual {p1}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->b()Ljava/lang/String;

    .line 251
    .line 252
    .line 253
    move-result-object v0

    .line 254
    invoke-interface {p2, v0, p1}, Ljava/util/Map;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    .line 255
    .line 256
    .line 257
    :cond_3
    :goto_1
    iget-object p2, p0, Lwkm;->d:Ljava/util/concurrent/atomic/AtomicReference;

    .line 258
    .line 259
    invoke-virtual {p2}, Ljava/util/concurrent/atomic/AtomicReference;->get()Ljava/lang/Object;

    .line 260
    .line 261
    .line 262
    move-result-object p2

    .line 263
    check-cast p2, Lwkl;

    .line 264
    .line 265
    new-instance v0, Lachb;

    .line 266
    .line 267
    invoke-direct {v0, p2}, Lachb;-><init>(Lwkl;)V

    .line 268
    .line 269
    .line 270
    iget-object v1, v0, Lachb;->d:Ljava/lang/Object;

    .line 271
    .line 272
    if-nez v1, :cond_4

    .line 273
    .line 274
    new-instance v1, Ljava/util/HashSet;

    .line 275
    .line 276
    invoke-direct {v1}, Ljava/util/HashSet;-><init>()V

    .line 277
    .line 278
    .line 279
    :cond_4
    iput-object v1, v0, Lachb;->d:Ljava/lang/Object;

    .line 280
    .line 281
    iget-object v1, v0, Lachb;->d:Ljava/lang/Object;

    .line 282
    .line 283
    invoke-interface {v1, p1}, Ljava/util/Set;->add(Ljava/lang/Object;)Z

    .line 284
    .line 285
    .line 286
    invoke-virtual {p0, p2, v0}, Lwkm;->F(Lwkl;Lachb;)Z

    .line 287
    .line 288
    .line 289
    move-result p2

    .line 290
    if-eqz p2, :cond_3

    .line 291
    .line 292
    :cond_5
    iget-object p2, p0, Lwkm;->e:Lbeqo;

    .line 293
    .line 294
    invoke-interface {p2}, Lbeqo;->a()Ljava/lang/Object;

    .line 295
    .line 296
    .line 297
    move-result-object p2

    .line 298
    move-object v3, p2

    .line 299
    check-cast v3, Laluq;

    .line 300
    .line 301
    if-nez p1, :cond_6

    .line 302
    .line 303
    sget-object p2, Lagho;->a:Laghp;

    .line 304
    .line 305
    goto :goto_2

    .line 306
    :cond_6
    move-object p2, p1

    .line 307
    :goto_2
    invoke-virtual {v3, p2}, Laluq;->bC(Laghp;)Lcom/google/common/util/concurrent/ListenableFuture;

    .line 308
    .line 309
    .line 310
    move-result-object p2

    .line 311
    invoke-static {p2}, Lamlm;->d(Lcom/google/common/util/concurrent/ListenableFuture;)Lamlm;

    .line 312
    .line 313
    .line 314
    move-result-object p2

    .line 315
    new-instance v0, Lulo;

    .line 316
    .line 317
    const/16 v1, 0xa

    .line 318
    .line 319
    invoke-direct {v0, v1}, Lulo;-><init>(I)V

    .line 320
    .line 321
    .line 322
    sget-object v1, Lansq;->a:Lansq;

    .line 323
    .line 324
    invoke-virtual {p2, v0, v1}, Lamlm;->g(Lamtm;Ljava/util/concurrent/Executor;)Lamlm;

    .line 325
    .line 326
    .line 327
    move-result-object p2

    .line 328
    new-instance v0, Lulo;

    .line 329
    .line 330
    const/16 v1, 0xb

    .line 331
    .line 332
    invoke-direct {v0, v1}, Lulo;-><init>(I)V

    .line 333
    .line 334
    .line 335
    sget-object v1, Lansq;->a:Lansq;

    .line 336
    .line 337
    const-class v2, Ljava/lang/Throwable;

    .line 338
    .line 339
    invoke-virtual {p2, v2, v0, v1}, Lamlm;->b(Ljava/lang/Class;Lamtm;Ljava/util/concurrent/Executor;)Lamlm;

    .line 340
    .line 341
    .line 342
    move-result-object p2

    .line 343
    new-instance v0, Lsyw;

    .line 344
    .line 345
    const/16 v4, 0x14

    .line 346
    .line 347
    const/4 v5, 0x0

    .line 348
    move-object v1, p0

    .line 349
    move-object v2, p1

    .line 350
    invoke-direct/range {v0 .. v5}, Lsyw;-><init>(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;I[B)V

    .line 351
    .line 352
    .line 353
    sget-object p1, Lansq;->a:Lansq;

    .line 354
    .line 355
    invoke-virtual {p2, v0, p1}, Lamlm;->h(Lansb;Ljava/util/concurrent/Executor;)Lamlm;

    .line 356
    .line 357
    .line 358
    move-result-object p1

    .line 359
    invoke-static {p1}, Laorz;->C(Lcom/google/common/util/concurrent/ListenableFuture;)Lcom/google/common/util/concurrent/ListenableFuture;

    .line 360
    .line 361
    .line 362
    move-result-object p1

    .line 363
    return-object p1
.end method

.method public final m(Ljava/lang/String;)Lcom/google/common/util/concurrent/ListenableFuture;
    .locals 4

    .line 1
    iget-object v0, p0, Lwkm;->d:Ljava/util/concurrent/atomic/AtomicReference;

    .line 2
    .line 3
    invoke-virtual {v0}, Ljava/util/concurrent/atomic/AtomicReference;->get()Ljava/lang/Object;

    .line 4
    .line 5
    .line 6
    move-result-object v0

    .line 7
    check-cast v0, Lwkl;

    .line 8
    .line 9
    invoke-virtual {v0}, Lwkl;->b()Z

    .line 10
    .line 11
    .line 12
    move-result v1

    .line 13
    if-eqz v1, :cond_0

    .line 14
    .line 15
    invoke-virtual {v0}, Lwkl;->a()Laghp;

    .line 16
    .line 17
    .line 18
    move-result-object v0

    .line 19
    invoke-interface {v0}, Laghp;->d()Ljava/lang/String;

    .line 20
    .line 21
    .line 22
    move-result-object v0

    .line 23
    iget-object v1, p0, Lwkm;->b:Lbeqo;

    .line 24
    .line 25
    invoke-interface {v1}, Lbeqo;->a()Ljava/lang/Object;

    .line 26
    .line 27
    .line 28
    move-result-object v1

    .line 29
    check-cast v1, Lxls;

    .line 30
    .line 31
    invoke-virtual {v1, v0}, Lxls;->l(Ljava/lang/String;)Lcom/google/common/util/concurrent/ListenableFuture;

    .line 32
    .line 33
    .line 34
    move-result-object v0

    .line 35
    new-instance v1, Lidv;

    .line 36
    .line 37
    const/16 v2, 0x9

    .line 38
    .line 39
    invoke-direct {v1, v2}, Lidv;-><init>(I)V

    .line 40
    .line 41
    .line 42
    invoke-static {v0, v1}, Lygz;->i(Lcom/google/common/util/concurrent/ListenableFuture;Lygy;)V

    .line 43
    .line 44
    .line 45
    :cond_0
    const/4 v0, 0x1

    .line 46
    invoke-virtual {p0, v0}, Lwkm;->n(Z)Lcom/google/common/util/concurrent/ListenableFuture;

    .line 47
    .line 48
    .line 49
    move-result-object v0

    .line 50
    invoke-static {v0}, Lamlm;->d(Lcom/google/common/util/concurrent/ListenableFuture;)Lamlm;

    .line 51
    .line 52
    .line 53
    move-result-object v0

    .line 54
    new-instance v1, Luxr;

    .line 55
    .line 56
    const/4 v2, 0x7

    .line 57
    const/4 v3, 0x0

    .line 58
    invoke-direct {v1, p0, p1, v2, v3}, Luxr;-><init>(Ljava/lang/Object;Ljava/lang/Object;I[B)V

    .line 59
    .line 60
    .line 61
    sget-object p1, Lansq;->a:Lansq;

    .line 62
    .line 63
    invoke-virtual {v0, v1, p1}, Lamlm;->h(Lansb;Ljava/util/concurrent/Executor;)Lamlm;

    .line 64
    .line 65
    .line 66
    move-result-object p1

    .line 67
    invoke-static {p1}, Laorz;->C(Lcom/google/common/util/concurrent/ListenableFuture;)Lcom/google/common/util/concurrent/ListenableFuture;

    .line 68
    .line 69
    .line 70
    move-result-object p1

    .line 71
    return-object p1
.end method

.method public final n(Z)Lcom/google/common/util/concurrent/ListenableFuture;
    .locals 1

    .line 1
    const/4 v0, 0x0

    .line 2
    invoke-virtual {p0, v0, p1}, Lwkm;->l(Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;Z)Lcom/google/common/util/concurrent/ListenableFuture;

    .line 3
    .line 4
    .line 5
    move-result-object p1

    .line 6
    return-object p1
.end method

.method public final o()Ljava/lang/String;
    .locals 3

    .line 1
    invoke-virtual {p0}, Lwkm;->x()Z

    .line 2
    .line 3
    .line 4
    move-result v0

    .line 5
    const/4 v1, 0x0

    .line 6
    if-eqz v0, :cond_0

    .line 7
    .line 8
    iget-object v0, p0, Lwkm;->a:Landroid/content/SharedPreferences;

    .line 9
    .line 10
    const-string v2, "incognito_visitor_id"

    .line 11
    .line 12
    invoke-interface {v0, v2, v1}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    .line 13
    .line 14
    .line 15
    move-result-object v0

    .line 16
    return-object v0

    .line 17
    :cond_0
    iget-object v0, p0, Lwkm;->a:Landroid/content/SharedPreferences;

    .line 18
    .line 19
    const-string v2, "visitor_id"

    .line 20
    .line 21
    invoke-interface {v0, v2, v1}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    .line 22
    .line 23
    .line 24
    move-result-object v0

    .line 25
    return-object v0
.end method

.method public final p([Landroid/accounts/Account;)Ljava/util/List;
    .locals 4

    .line 1
    invoke-static {}, Lqxs;->aX()V

    .line 2
    .line 3
    .line 4
    invoke-virtual {p1}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    .line 5
    .line 6
    .line 7
    array-length v0, p1

    .line 8
    new-array v1, v0, [Ljava/lang/String;

    .line 9
    .line 10
    const/4 v2, 0x0

    .line 11
    :goto_0
    if-ge v2, v0, :cond_0

    .line 12
    .line 13
    aget-object v3, p1, v2

    .line 14
    .line 15
    iget-object v3, v3, Landroid/accounts/Account;->name:Ljava/lang/String;

    .line 16
    .line 17
    aput-object v3, v1, v2

    .line 18
    .line 19
    add-int/lit8 v2, v2, 0x1

    .line 20
    .line 21
    goto :goto_0

    .line 22
    :cond_0
    iget-object p1, p0, Lwkm;->c:Lwkh;

    .line 23
    .line 24
    invoke-interface {p1, v1}, Lwkh;->g([Ljava/lang/String;)Ljava/util/List;

    .line 25
    .line 26
    .line 27
    move-result-object p1

    .line 28
    return-object p1
.end method

.method public final q()V
    .locals 3

    .line 1
    :cond_0
    iget-object v0, p0, Lwkm;->d:Ljava/util/concurrent/atomic/AtomicReference;

    .line 2
    .line 3
    invoke-virtual {v0}, Ljava/util/concurrent/atomic/AtomicReference;->get()Ljava/lang/Object;

    .line 4
    .line 5
    .line 6
    move-result-object v0

    .line 7
    check-cast v0, Lwkl;

    .line 8
    .line 9
    invoke-virtual {v0}, Lwkl;->b()Z

    .line 10
    .line 11
    .line 12
    move-result v1

    .line 13
    if-eqz v1, :cond_1

    .line 14
    .line 15
    new-instance v1, Lachb;

    .line 16
    .line 17
    invoke-direct {v1, v0}, Lachb;-><init>(Lwkl;)V

    .line 18
    .line 19
    .line 20
    sget-object v2, Lwkw;->a:Lwkw;

    .line 21
    .line 22
    iput-object v2, v1, Lachb;->a:Ljava/lang/Object;

    .line 23
    .line 24
    invoke-virtual {p0, v0, v1}, Lwkm;->F(Lwkl;Lachb;)Z

    .line 25
    .line 26
    .line 27
    move-result v0

    .line 28
    if-eqz v0, :cond_0

    .line 29
    .line 30
    :cond_1
    return-void
.end method

.method public final r(Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;)V
    .locals 3

    .line 1
    :cond_0
    iget-object v0, p0, Lwkm;->d:Ljava/util/concurrent/atomic/AtomicReference;

    .line 2
    .line 3
    invoke-virtual {v0}, Ljava/util/concurrent/atomic/AtomicReference;->get()Ljava/lang/Object;

    .line 4
    .line 5
    .line 6
    move-result-object v0

    .line 7
    check-cast v0, Lwkl;

    .line 8
    .line 9
    invoke-virtual {v0}, Lwkl;->a()Laghp;

    .line 10
    .line 11
    .line 12
    move-result-object v1

    .line 13
    invoke-interface {v1}, Laghp;->d()Ljava/lang/String;

    .line 14
    .line 15
    .line 16
    move-result-object v1

    .line 17
    invoke-virtual {p1}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->d()Ljava/lang/String;

    .line 18
    .line 19
    .line 20
    move-result-object v2

    .line 21
    invoke-virtual {v1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    .line 22
    .line 23
    .line 24
    move-result v1

    .line 25
    if-eqz v1, :cond_1

    .line 26
    .line 27
    new-instance v1, Lachb;

    .line 28
    .line 29
    invoke-direct {v1, v0}, Lachb;-><init>(Lwkl;)V

    .line 30
    .line 31
    .line 32
    sget-object v2, Lwkw;->a:Lwkw;

    .line 33
    .line 34
    iput-object v2, v1, Lachb;->a:Ljava/lang/Object;

    .line 35
    .line 36
    invoke-virtual {p0, v0, v1}, Lwkm;->F(Lwkl;Lachb;)Z

    .line 37
    .line 38
    .line 39
    move-result v0

    .line 40
    if-eqz v0, :cond_0

    .line 41
    .line 42
    :cond_1
    iget-object v0, p0, Lwkm;->c:Lwkh;

    .line 43
    .line 44
    invoke-virtual {p1}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->d()Ljava/lang/String;

    .line 45
    .line 46
    .line 47
    move-result-object p1

    .line 48
    invoke-interface {v0, p1}, Lwkh;->i(Ljava/lang/String;)V

    .line 49
    .line 50
    .line 51
    return-void
.end method

.method public final s(Lagha;Ljava/lang/String;)V
    .locals 1

    .line 1
    iget-boolean v0, p0, Lwkm;->g:Z

    .line 2
    .line 3
    if-eqz v0, :cond_0

    .line 4
    .line 5
    invoke-static {p1, p2}, Lwkm;->z(Lagha;Ljava/lang/String;)V

    .line 6
    .line 7
    .line 8
    :cond_0
    return-void
.end method

.method public final t(Ljava/util/List;)V
    .locals 4

    .line 1
    invoke-static {}, Lqxs;->aX()V

    .line 2
    .line 3
    .line 4
    invoke-interface {p1}, Ljava/util/List;->size()I

    .line 5
    .line 6
    .line 7
    move-result v0

    .line 8
    new-array v1, v0, [Ljava/lang/String;

    .line 9
    .line 10
    const/4 v2, 0x0

    .line 11
    :goto_0
    if-ge v2, v0, :cond_0

    .line 12
    .line 13
    invoke-interface {p1, v2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    .line 14
    .line 15
    .line 16
    move-result-object v3

    .line 17
    check-cast v3, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;

    .line 18
    .line 19
    invoke-virtual {v3}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->a()Ljava/lang/String;

    .line 20
    .line 21
    .line 22
    move-result-object v3

    .line 23
    aput-object v3, v1, v2

    .line 24
    .line 25
    add-int/lit8 v2, v2, 0x1

    .line 26
    .line 27
    goto :goto_0

    .line 28
    :cond_0
    iget-object p1, p0, Lwkm;->c:Lwkh;

    .line 29
    .line 30
    invoke-interface {p1, v1}, Lwkh;->h([Ljava/lang/String;)V

    .line 31
    .line 32
    .line 33
    return-void
.end method

.method public final u(Ljava/lang/String;Ljava/lang/String;)V
    .locals 4

    .line 1
    :cond_0
    iget-object v0, p0, Lwkm;->d:Ljava/util/concurrent/atomic/AtomicReference;

    .line 2
    .line 3
    invoke-virtual {v0}, Ljava/util/concurrent/atomic/AtomicReference;->get()Ljava/lang/Object;

    .line 4
    .line 5
    .line 6
    move-result-object v0

    .line 7
    check-cast v0, Lwkl;

    .line 8
    .line 9
    invoke-virtual {v0}, Lwkl;->b()Z

    .line 10
    .line 11
    .line 12
    move-result v1

    .line 13
    if-eqz v1, :cond_2

    .line 14
    .line 15
    iget-object v1, v0, Lwkl;->b:Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;

    .line 16
    .line 17
    invoke-virtual {v1}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->a()Ljava/lang/String;

    .line 18
    .line 19
    .line 20
    move-result-object v1

    .line 21
    invoke-virtual {p1, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    .line 22
    .line 23
    .line 24
    move-result v1

    .line 25
    if-nez v1, :cond_1

    .line 26
    .line 27
    goto :goto_0

    .line 28
    :cond_1
    iget-object v1, v0, Lwkl;->b:Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;

    .line 29
    .line 30
    invoke-virtual {v1}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->d()Ljava/lang/String;

    .line 31
    .line 32
    .line 33
    move-result-object v2

    .line 34
    invoke-virtual {v1}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->e()Ljava/lang/String;

    .line 35
    .line 36
    .line 37
    move-result-object v3

    .line 38
    invoke-virtual {v1}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->b()Ljava/lang/String;

    .line 39
    .line 40
    .line 41
    move-result-object v1

    .line 42
    invoke-static {v2, p2, v3, v1}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->n(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;

    .line 43
    .line 44
    .line 45
    move-result-object v1

    .line 46
    new-instance v2, Lachb;

    .line 47
    .line 48
    invoke-direct {v2, v0}, Lachb;-><init>(Lwkl;)V

    .line 49
    .line 50
    .line 51
    iput-object v1, v2, Lachb;->e:Ljava/lang/Object;

    .line 52
    .line 53
    invoke-virtual {p0, v0, v2}, Lwkm;->F(Lwkl;Lachb;)Z

    .line 54
    .line 55
    .line 56
    move-result v0

    .line 57
    if-eqz v0, :cond_0

    .line 58
    .line 59
    iget-object v0, p0, Lwkm;->a:Landroid/content/SharedPreferences;

    .line 60
    .line 61
    invoke-interface {v0}, Landroid/content/SharedPreferences;->edit()Landroid/content/SharedPreferences$Editor;

    .line 62
    .line 63
    .line 64
    move-result-object v0

    .line 65
    const-string v1, "user_account"

    .line 66
    .line 67
    invoke-interface {v0, v1, p2}, Landroid/content/SharedPreferences$Editor;->putString(Ljava/lang/String;Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;

    .line 68
    .line 69
    .line 70
    move-result-object v0

    .line 71
    invoke-interface {v0}, Landroid/content/SharedPreferences$Editor;->apply()V

    .line 72
    .line 73
    .line 74
    :cond_2
    :goto_0
    iget-object v0, p0, Lwkm;->c:Lwkh;

    .line 75
    .line 76
    invoke-interface {v0, p1, p2}, Lwkh;->j(Ljava/lang/String;Ljava/lang/String;)V

    .line 77
    .line 78
    .line 79
    return-void
.end method

.method public final v(Lwkw;)V
    .locals 3

    .line 1
    :cond_0
    iget-object v0, p0, Lwkm;->d:Ljava/util/concurrent/atomic/AtomicReference;

    .line 2
    .line 3
    invoke-virtual {v0}, Ljava/util/concurrent/atomic/AtomicReference;->get()Ljava/lang/Object;

    .line 4
    .line 5
    .line 6
    move-result-object v0

    .line 7
    check-cast v0, Lwkl;

    .line 8
    .line 9
    invoke-virtual {v0}, Lwkl;->b()Z

    .line 10
    .line 11
    .line 12
    move-result v1

    .line 13
    if-eqz v1, :cond_1

    .line 14
    .line 15
    iget-object v1, v0, Lwkl;->b:Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;

    .line 16
    .line 17
    new-instance v2, Lachb;

    .line 18
    .line 19
    invoke-direct {v2, v0}, Lachb;-><init>(Lwkl;)V

    .line 20
    .line 21
    .line 22
    iput-object p1, v2, Lachb;->a:Ljava/lang/Object;

    .line 23
    .line 24
    invoke-virtual {p0, v0, v2}, Lwkm;->F(Lwkl;Lachb;)Z

    .line 25
    .line 26
    .line 27
    move-result v0

    .line 28
    if-eqz v0, :cond_0

    .line 29
    .line 30
    iget-object v0, p0, Lwkm;->c:Lwkh;

    .line 31
    .line 32
    invoke-virtual {v1}, Lcom/google/android/libraries/youtube/account/identity/AccountIdentity;->d()Ljava/lang/String;

    .line 33
    .line 34
    .line 35
    move-result-object v1

    .line 36
    invoke-interface {v0, v1, p1}, Lwkh;->k(Ljava/lang/String;Lwkw;)V

    .line 37
    .line 38
    .line 39
    :cond_1
    return-void
.end method

.method public final w()Z
    .locals 3

    .line 1
    iget-object v0, p0, Lwkm;->a:Landroid/content/SharedPreferences;

    .line 2
    .line 3
    const-string v1, "user_signed_out"

    .line 4
    .line 5
    const/4 v2, 0x0

    .line 6
    invoke-interface {v0, v1, v2}, Landroid/content/SharedPreferences;->getBoolean(Ljava/lang/String;Z)Z

    .line 7
    .line 8
    .line 9
    move-result v0

    .line 10
    return v0
.end method

.method public final x()Z
    .locals 3

    .line 1
    iget-object v0, p0, Lwkm;->a:Landroid/content/SharedPreferences;

    .line 2
    .line 3
    const-string v1, "incognito_visitor_id"

    .line 4
    .line 5
    const/4 v2, 0x0

    .line 6
    invoke-interface {v0, v1, v2}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    .line 7
    .line 8
    .line 9
    move-result-object v0

    .line 10
    if-eqz v0, :cond_0

    .line 11
    .line 12
    const/4 v0, 0x1

    .line 13
    return v0

    .line 14
    :cond_0
    const/4 v0, 0x0

    .line 15
    return v0
.end method

.method public final y()Z
    .locals 1

    .line 1
    iget-object v0, p0, Lwkm;->d:Ljava/util/concurrent/atomic/AtomicReference;

    .line 2
    .line 3
    invoke-virtual {v0}, Ljava/util/concurrent/atomic/AtomicReference;->get()Ljava/lang/Object;

    .line 4
    .line 5
    .line 6
    move-result-object v0

    .line 7
    check-cast v0, Lwkl;

    .line 8
    .line 9
    invoke-virtual {v0}, Lwkl;->b()Z

    .line 10
    .line 11
    .line 12
    move-result v0

    .line 13
    return v0
.end method
