# aatree
## Performance of AATree in comparison to immutable.TreeSet

For small collections both performs similarly, but for larger number of elements (>512) AATree performs better 

According to the benchmarks: 

1. Insert all elements ~ 0.7x faster
2. Remove all elements ~ 4.4x faster
3. Max ~ 10000x faster
4. Min ~ 10000x faster
5. Calling contains for every elem ~ 0.13x faster 
6. Foreach ~ 30.0x faster
7. Iterates through all elems using iterator ~ 45.0x faster
8. Union of two trees(++) ~ 98.0x faster
9. Map ~ 140.0x faster
10. FlatMap ~ 170.0x faster
  
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