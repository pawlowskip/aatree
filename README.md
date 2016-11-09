# aatree
## Performance of AATree in comparison to immutable.TreeSet
For small collections both performs similarly, but for larger number of elements (>512) AATree performs better 
According to the benchmarks: 
1. insert all elements ~ 0.7x faster
2. remove all elements ~ 4.4x faster
3. max ~ 10000x faster
4. min ~ 10000x faster
5. calling contains for every elem ~ 0.13x faster 
6. foreach ~ 30.0x faster
7. iterates through all elems using iterator ~ 45.0x faster
8. union of two trees(++) ~ 98.0x faster
9. map ~ 140.0x faster
10. flatMap ~ 170.0x faster
  
## Memory usage in bytes
|Size     |TreeSet   |AATree    |
|---------|----------|----------|
|0        |40        |32        |
|1        |104       |96        |
|4        |248       |240       |
|16       |824       |816       |
|64       |3,128     |3,120     |
|256      |12,344    |12,336    |
|1,024    |49,208    |49,200    |
|4,069    |195,368   |195,360   |
|16,192   |777,272   |777,264   | 
|65,536   |3,145,784 |3,145,776 |
|262,144  |12,582,968|12,582,960|   
|1,048,576|50,331,704|50,331,696|