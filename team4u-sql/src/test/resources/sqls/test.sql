/* demo1 */
SELECT *
FROM client;

/* TestMapper.eventOf */
select *
from stored_event
#if(id)
where id = #{id}
#end
#if(none)
  and id !=
#{id}
#end