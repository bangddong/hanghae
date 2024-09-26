# 동시성 제어

### 동시성 제어란?

여러 프로세스나 스레드가 동시에 실행될 때 이를 비즈니스 로직이나 프로그램의 안정성을 해치지 않도록 제어하는 것을 말합니다.  
동시성 제어는 프로그램의 안정성을 유지하고, 데이터의 무결성을 보장하며, 프로그램의 성능을 향상시키는데 중요한 역할을 하게 됩니다.

그렇다면, 동시성 제어가 제대로 되지 않는다면 어떤 일들이 일어날까요?

A 유저 통장 잔액이 현재 10,000원이라고 가정해보겠습니다.

인터넷 쇼핑 중 특정 상품을 결제하려고 할 때 다음과 같은 일들이 일어날 수 있습니다.

````
1. A 유저가 상품을 결제하기 위해 결제 버튼을 클릭합니다.
2. 결제 버튼을 클릭한 시점에서 A 유저의 통장 잔액은 10,000원입니다.
3. 결제 버튼을 클릭한 시점에서 A 유저의 통장 잔액이 10,000원이라는 것을 확인한 서버는 결제를 진행합니다.
4. 결제가 진행되는 동안 A 유저가 다시 결제 버튼을 클릭합니다.
5. 결제가 진행되는 동안 A 유저의 통장 잔액이 10,000원이라는 것을 확인한 서버는 결제를 진행합니다.
6. 결제가 완료되고, A 유저의 통장 잔액이 10,000원에서 5,000원으로 변경됩니다.
````

너무 극단적인 예시지만 동시성 제어의 실패로 쇼핑몰은 강제 1+1 행사가 되겠죠?

위처럼 값의 반영이 이뤄지기 전에 다른 프로세스나 스레드 또한 로직을 처리하기 위해 값을 읽어간다면  
위와 같이 의도하지 않은 상황이 발생할 수도 있습니다.

그렇다면 로직상에서 어떤 방법들을 통해 동시성 제어를 할 수 있을까요?

<br/>

### Synchronized

Synchronized는 자바에서 제공하는 동기화 방법 중 하나입니다.

Lock을 기반으로 동작하기에 특정 객체 또는 함수에 대해 Lock을 걸어 다른 스레드가 접근하지 못하도록 합니다.

````
A 스레드가 Synchronized 함수를 호출하며 Lock을 획득합니다.
B 스레드가 Synchronized 함수를 호출하려고 하지만 Lock이 획득되어 있기에 대기합니다.
A 스레드가 Synchronized 함수를 실행을 마치고 Lock을 반환합니다.
B 스레드가 Lock을 획득하고 Synchronized 함수를 실행합니다.
````

위 예시처럼 한 번에 오직 하나의 스레드만 접근할 수 있기 때문에 동시성 제어를 할 수 있습니다.

그럼 이제 문제가 해결되었을까요?

아래 또 다른 예시를 보시죠.

````
A 유저가 상품을 결제하기 위해 결제 버튼을 클릭합니다.
동시에 B 유저가 상품을 결제하기 위해 결제 버튼을 클릭합니다.
B 유저는 A 유저의 결제가 처리될 때까지 기다립니다.
````

위와 같은 상황에서 A 유저의 결제가 처리되기 전까지 B 유저는 결제를 진행할 수 없습니다.

A 유저의 잔고와 B 유저의 잔고는 서로 다르며 `공유자원`에 대한 접근이 아닙니다.

그럼에도 불구하고 한 번에 한 번씩 모든 처리에 대해 Lock을 걸어두면 상당히 비효율적일 수 있습니다.

그 외에 단순 읽기 작업에도 Lock을 건다거나, 잘못된 설계로 서로 Lock을 획득하려는 `DeadLock`이 발생할 수도 있습니다.

<br/>  

✅ 사용 예제

```java
public synchronized UserPointResponse charge(long id, long amount) { // synchronized 키워드 사용
	final UserPoint updatedUserPoint = cache.compute(id, (key, existingUserPoint) -> {
		if (existingUserPoint == null) {
			existingUserPoint = pointReader.read(id);
		}
		return pointManager.charge(id, amount);
	});
	historyManager.append(id, amount, TransactionType.CHARGE, System.currentTimeMillis());
	return UserPointResponse.fromEntity(updatedUserPoint);
}

```

<br/>

### ConcurrentHashMap

이번 과제에서 동시성 제어를 위해 처음 선택한 방법입니다만 순차성 처리는 보장되지 않아 사용하지 않게 되었습니다.

ConcurrentHashMap은 `Bucket` 단위의 락과 `CAS`를 사용하여 동시성을 제어합니다.

먼저 `Bucket`은 쉽게 말하면 배열이며 각 노드는 키-값으로 이루어진 해시테이블이며  
이 `Bucket`은 독립적으로 락을 가지고 있기 때문에 다른 `Bucket`에 대한 접근은 Lock이 걸리지 않고 효율적인 연산을 할 수 있게됩니다.

`CAS`는 Compare And Swap의 약자로, 동시에 여러 스레드에 의해 같은 메모리 주소에 대한 값을 변경하려는 시도가 있을 때,  
데이터의 일관성을 보장하는 알고리즘입니다.

간단한 동작 원리로는 단어 그대로 `비교 후 교체`인데, 현재 스레드에 의해 읽어온 값과 메모리에 저장된 값이 같다면  
스레드는 값을 변경하고, 다르다면 다시 읽어와서 비교하는 과정을 반복하여 원자성을 보장합니다.

ConcurrentHashMap 사용시에 PointManager는 다음과 같이 동시성을 제어합니다.

```
[Thread1] charge 요청
    ⬇
[Thread1] key에 해당하는 버킷을 찾아 잔액 확인하며 Read는 Lock 없이 진행
    ⬇
[Thread1] Lock 획득 후 Update
    ⬇
[Thread2] use 요청
    ⬇
[Thread2] key에 해당하는 버킷을 찾았으나 Thread1에 의해 해당 노드가 잠겨있으니 대기
    ⬇
[Thread1] Charge 완료 후 Lock 해제
    ⬇
[Thread2] Lock 획득 후 잔액 조회 및 Use
    ⬇
[Thread2] Use 완료 후 Lock 해제 
```

읽기는 Lock이 안걸린다며???

사실 ConcurrentHashMap도 읽기 작업에 대해 Lock이 걸릴 수 있습니다.

현재 ConcurrentHashMap으로 구현된 PointService를 가져와보겠습니다.

<br />  

✅ 사용 예제

```java
private final ConcurrentHashMap<Long, UserPoint> cache = new ConcurrentHashMap<>();

public UserPointResponse charge(long id, long amount) {
	final UserPoint updatedUserPoint = cache.compute(id, (key, existingUserPoint) -> {
		if (existingUserPoint == null) {
			existingUserPoint = pointReader.read(id);
		}
		return pointManager.charge(id, amount);
	});
	historyManager.append(id, amount, TransactionType.CHARGE, System.currentTimeMillis());
	return UserPointResponse.fromEntity(updatedUserPoint);
}
```

포인트의 충전은 현재 잔액을 읽는 `pointReader.read`와 `pointManager.charge`가 원자적으로 이루어져야 합니다.

그래야 동시에 다른 충전 요청이나 사용 요청이 와도 데이터의 무결성을 보장할 수 있기 때문입니다.

조금 더 풀어서 얘기하자면,

모든 요청이 단순 `read` 작업만 수행하거나 key로 사용되는 id가 다른 경우에는 Lock을 걸지않아 성능상 이점을 가져가면서도,   
`cache.compute`와 같이 원자적으로 이루어져야 하는 작업에 대해서는 해당 노드에 Lock을 통해 `read` 또한 같이 접근할 수 없게 만들어  
동시성 문제가 발생하지 않게 되는겁니다.

### ReentrantLock

`Synchronized`와 같이 자바에서 제공하는 동기화 방법 중 하나입니다.

다만, `Synchronized`와는 다르게 Lock을 직접 생성하여 사용할 수 있기 때문에 조금 더 세밀한 제어가 가능합니다.

먼저 사용 방법부터가 다른데 `Synchronized`는 객체나 함수에 대해 Lock을 걸어 사용하는 방식이었다면  
`ReentrantLock`은 Lock 객체를 생성하여 사용하며 명시적으로 `lock()`과 `unlock()`을 호출하여 사용합니다.

따라서 Lock을 걸어야 하는 시점을 조금 더 세밀하게 제어할 수 있게 됩니다.

또한, 공정성 설정이 가능하기 때문에 Queue와 같이 FIFO 방식으로 요청 순서에 따라 Lock을 획득하게 할 수 있으며  
이는 순차적인 처리가 필요한 서비스에서 유용하게 사용될 수 있습니다.

<br />  

✅ 사용 예제

```java
private final ReentrantLock lock = new ReentrantLock();  // ReentrantLock 생성

public UserPointResponse charge(long id, long amount) {
	final UserPoint updatedUserPoint;
	lock.lock(); // 락 획득
	try {
		updatedUserPoint = pointManager.charge(id, amount);
	} finally {
		lock.unlock(); // 반드시 락 해제
	}

	historyManager.append(id, amount, TransactionType.CHARGE, System.currentTimeMillis());
	return UserPointResponse.fromEntity(updatedUserPoint);
}
```